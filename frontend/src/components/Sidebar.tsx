import { Link, useLocation } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { Bell, History, BarChart3 } from "lucide-react";
import { NotificationBell } from "./NotificationBell";

export const Sidebar = () => {
  const location = useLocation();

  return (
    <div className="fixed left-0 top-0 h-screen w-[240px] bg-[#0A2846] flex flex-col pt-8 z-10">
      {/* Campana de notificaciones */}
      <div className="px-4 mb-4 flex justify-end">
        <NotificationBell />
      </div>

      <div className="flex flex-col space-y-2 px-4">
        <Button
          variant="ghost"
          className={`justify-start text-white hover:bg-white/10 h-12 px-4 ${
            location.pathname === "/dashboard" ? "bg-white/10" : ""
          }`}
          asChild
        >
          <Link to="/dashboard" className="flex items-center gap-3">
            <Bell className="h-5 w-5" />
            <span className="text-base font-medium">Dashboard</span>
          </Link>
        </Button>
        
        <Button
          variant="ghost"
          className={`justify-start text-white hover:bg-white/10 h-12 px-4 ${
            location.pathname === "/panel-de-alertas" ? "bg-white/10" : ""
          }`}
          asChild
        >
          <Link to="/panel-de-alertas" className="flex items-center gap-3">
            <History className="h-5 w-5" />
            <span className="text-base font-medium">Historial</span>
          </Link>
        </Button>

        <Button
          variant="ghost"
          className={`justify-start text-white hover:bg-white/10 h-12 px-4 ${
            location.pathname === "/estadisticas" ? "bg-white/10" : ""
          }`}
          asChild
        >
          <Link to="/estadisticas" className="flex items-center gap-3">
            <BarChart3 className="h-5 w-5" />
            <span className="text-base font-medium">Estadísticas</span>
          </Link>
        </Button>
      </div>
    </div>
  );
};
