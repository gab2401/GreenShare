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

        // 1. Preflight CORS: le richieste OPTIONS passano sempre
        if ("OPTIONS".equalsIgnoreCase(method)) {
            chain.doFilter(request, response);
            return;
        }

        // 2. Rotte pubbliche
        if (path.startsWith("/api/auth") ||
                ("GET".equalsIgnoreCase(method) && (path.startsWith("/api/orti") || path.startsWith("/api/attivita")))) {
            chain.doFilter(request, response);
            return;
        }

        // 3. Controllo Sessione (401 Unauthorized se non loggato)
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("LOGGED_USER_ID") == null) {
            aggiungiHeaderCors(res);
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType("application/json");
            res.getWriter().write("{\"error\": \"Accesso non autorizzato. Effettuare prima il login.\"}");
            return;
        }

        // 4. Controllo Ruolo per rotte riservate al Gestore (403 Forbidden)
        boolean isAdminRoute =
                ("GET".equalsIgnoreCase(method) && path.equals("/api/utenti")) ||
                        ("PUT".equalsIgnoreCase(method) && path.startsWith("/api/prenotazioni/") && path.endsWith("/approva")) ||
                        ("DELETE".equalsIgnoreCase(method) && path.startsWith("/api/prenotazioni/")) ||
                        ("POST".equalsIgnoreCase(method) && path.startsWith("/api/orti/") && path.endsWith("/attrezzi"));

        String role = (String) session.getAttribute("LOGGED_USER_ROLE");
        if (isAdminRoute && !"ROLE_GESTORE".equals(role)) {
            aggiungiHeaderCors(res);
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            res.setContentType("application/json");
            res.getWriter().write("{\"error\": \"Permessi insufficienti: azione riservata al Gestore.\"}");
            return;
        }

        chain.doFilter(request, response);
    }

    private void aggiungiHeaderCors(HttpServletResponse res) {
        res.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        res.setHeader("Access-Control-Allow-Credentials", "true");
    }
}