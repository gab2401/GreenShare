package it.unito.greenshare.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();
        String method = req.getMethod();

        // 1. Gestione Preflight CORS: le richieste OPTIONS del browser passano sempre
        if ("OPTIONS".equalsIgnoreCase(method)) {
            chain.doFilter(request, response);
            return;
        }

        // 2. Rotte pubbliche: consultazione orti e autenticazione non richiedono login
        if (path.startsWith("/api/auth") ||
                ("GET".equalsIgnoreCase(method) && (path.startsWith("/api/orti") || path.startsWith("/api/attivita")))) {
            chain.doFilter(request, response);
            return;
        }

        // 3. Controllo della Sessione per tutte le altre rotte protette (POST, PUT, ecc.)
        HttpSession session = req.getSession(false); // false: non crea una sessione nuova se non esiste
        if (session != null && session.getAttribute("LOGGED_USER_ID") != null) {
            // Utente autenticato: la richiesta prosegue verso il Controller
            chain.doFilter(request, response);
        } else {
            // Utente non autenticato: blocco immediato con codice 401 Unauthorized
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType("application/json");
            res.getWriter().write("{\"error\": \"Accesso non autorizzato. Effettuare prima il login.\"}");
        }
    }
}