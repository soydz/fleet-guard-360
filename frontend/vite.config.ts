import { defineConfig } from "vite";
import react from "@vitejs/plugin-react-swc";
import path from "path";
import { componentTagger } from "lovable-tagger";

// https://vitejs.dev/config/
export default defineConfig(({ mode }) => ({
  server: {
    host: "::",
    port: 3000,
    proxy: {
      "/api": {
        target: "http://fleetguard-api.ddns.net",
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ""),
        configure: (proxy, _options) => {
          proxy.on("proxyReq", (proxyReq, _req, _res) => {
            proxyReq.setHeader("Origin", "http://fleetguard-api.ddns.net");
          });
        },
      },
      "/notifications": {
        target: "http://fleetguard-api.ddns.net",
        changeOrigin: true,
        ws: true, // Importante: habilita el proxy de WebSocket
        secure: false,
      },
    },
  },
  plugins: [react(), mode === "development" && componentTagger()].filter(Boolean),
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "./src"),
    },
  },
  define: {
    global: "globalThis",
  },
}));
