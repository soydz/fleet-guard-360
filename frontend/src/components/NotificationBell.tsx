import { useState } from "react";
import { Bell } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover";
import { ScrollArea } from "@/components/ui/scroll-area";
import { useWebSocketNotifications } from "@/hooks/use-websocket-notifications";
import { useToast } from "@/hooks/use-toast";

export const NotificationBell = () => {
  const { toast } = useToast();
  const [isOpen, setIsOpen] = useState(false);

  const { notifications, isConnected, clearNotifications } = useWebSocketNotifications({
    onNotification: (notification) => {
      // Mostrar toast cuando llegue una notificación
      toast({
        title: "Nueva Alerta",
        description: `${notification.alertType} - ${notification.generatingUnit}`,
        variant: notification.priority?.toLowerCase().includes("alta") ? "destructive" : "default",
      });
    },
  });

  const unreadCount = notifications.length;

  const formatDate = (isoDate: string) => {
    try {
      const date = new Date(isoDate);
      const now = new Date();
      const diff = now.getTime() - date.getTime();
      const minutes = Math.floor(diff / 60000);
      const hours = Math.floor(diff / 3600000);
      const days = Math.floor(diff / 86400000);

      if (minutes < 1) return "Ahora";
      if (minutes < 60) return `Hace ${minutes}m`;
      if (hours < 24) return `Hace ${hours}h`;
      return `Hace ${days}d`;
    } catch {
      return "";
    }
  };

  const getPriorityColor = (priority: string) => {
    const prioridadLower = priority?.toLowerCase() || "";
    if (prioridadLower.includes("alta")) return "bg-red-600 text-white";
    if (prioridadLower.includes("media")) return "bg-yellow-500 text-gray-900";
    if (prioridadLower.includes("baja")) return "bg-green-600 text-white";
    return "bg-gray-500 text-white";
  };

  return (
    <Popover open={isOpen} onOpenChange={setIsOpen}>
      <PopoverTrigger asChild>
        <Button
          variant="ghost"
          size="icon"
          className="relative text-white hover:bg-white/10 h-10 w-10"
        >
          <Bell className="h-5 w-5" />
          {unreadCount > 0 && (
            <Badge className="absolute -top-1 -right-1 h-5 w-5 flex items-center justify-center p-0 bg-red-600 text-white text-xs">
              {unreadCount > 9 ? "9+" : unreadCount}
            </Badge>
          )}
          {/* Indicador de conexión: verde si conectado, rojo si desconectado */}
          <span className={`absolute bottom-0 right-0 h-2 w-2 rounded-full ${isConnected ? 'bg-green-500' : 'bg-red-500'}`} />
        </Button>
      </PopoverTrigger>
      <PopoverContent className="w-80 bg-[#141C2F] border-gray-700 p-0" align="end">
        <div className="flex items-center justify-between p-4 border-b border-gray-700">
          <h3 className="font-semibold text-white">Notificaciones</h3>
          {unreadCount > 0 && (
            <Button
              variant="ghost"
              size="sm"
              onClick={clearNotifications}
              className="text-xs text-gray-400 hover:text-white"
            >
              Limpiar
            </Button>
          )}
        </div>

        <ScrollArea className="h-[400px]">
          {notifications.length === 0 ? (
            <div className="flex flex-col items-center justify-center py-8 text-gray-400">
              <Bell className="h-12 w-12 mb-2 opacity-50" />
              <p className="text-sm">No hay notificaciones</p>
            </div>
          ) : (
            <div className="divide-y divide-gray-700">
              {notifications.map((notification, index) => (
                <div
                  key={`${notification.id || index}`}
                  className="p-4 hover:bg-white/5 transition-colors"
                >
                  <div className="flex items-start justify-between gap-3">
                    <div className="flex-1 min-w-0">
                      <h4 className="text-sm font-medium text-white mb-2">
                        {notification.alertType}
                      </h4>
                      <p className="text-xs text-gray-400 mb-1">
                        📍 {notification.generatingUnit}
                      </p>
                      {notification.driver && (
                        <p className="text-xs text-gray-400 mb-1">
                          👤 Conductor: {notification.driver}
                        </p>
                      )}
                      {notification.responsible && (
                        <p className="text-xs text-gray-400 mb-1">
                          👨‍💼 Responsable: {notification.responsible}
                        </p>
                      )}
                      {(notification.statusAlert || notification.state) && (
                        <p className="text-xs text-gray-400 mt-2">
                          ⚡ Estado: {notification.statusAlert || notification.state}
                        </p>
                      )}
                      <p className="text-xs text-gray-500 mt-2">
                        🕐 {formatDate(notification.generationDate)}
                      </p>
                    </div>
                    {notification.priority && (
                      <Badge className={`${getPriorityColor(notification.priority)} text-xs px-2 py-1 h-fit whitespace-nowrap`}>
                        {notification.priority}
                      </Badge>
                    )}
                  </div>
                </div>
              ))}
            </div>
          )}
        </ScrollArea>

        <div className="p-3 border-t border-gray-700 bg-[#0A2846]">
          <div className="flex items-center gap-2 text-xs text-gray-400">
            <div className={`h-2 w-2 rounded-full ${isConnected ? "bg-green-500" : "bg-yellow-500"}`} />
            <span>{isConnected ? "Conectado" : "Reconectando..."}</span>
          </div>
        </div>
      </PopoverContent>
    </Popover>
  );
};
