import { useEffect, useRef, useState } from "react";
import SockJS from "sockjs-client";
import Stomp from "stompjs";

export interface AlertNotification {
  id?: string;
  alertType: string;
  responsible?: string;
  priority?: string;
  driver?: string;
  generatingUnit: string;
  state?: string;
  statusAlert?: string;
  generationDate: string;
  message?: string;
}

interface UseWebSocketNotificationsProps {
  onNotification?: (notification: AlertNotification) => void;
  autoConnect?: boolean;
}

export const useWebSocketNotifications = ({
  onNotification,
  autoConnect = true,
}: UseWebSocketNotificationsProps = {}) => {
  const [isConnected, setIsConnected] = useState(false);
  const [notifications, setNotifications] = useState<AlertNotification[]>([]);
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const stompClientRef = useRef<any>(null);

  const connect = () => {
    try {
      // Usar ruta relativa para que funcione con el proxy de Vercel
      const socket = new SockJS("/notifications/ws-alerts");
      const stompClient = Stomp.over(socket);

      // Desactivar logs de debug (opcional)
      stompClient.debug = () => {};

      stompClient.connect(
        {},
        // eslint-disable-next-line @typescript-eslint/no-explicit-any
        (frame: any) => {
          console.log("WebSocket conectado:", frame);
          setIsConnected(true);

          // Suscribirse al canal de alertas
          // eslint-disable-next-line @typescript-eslint/no-explicit-any
          stompClient.subscribe("/all/alerts", (message) => {
            try {
              console.log("Mensaje RAW recibido:", message.body);
              const alert: AlertNotification = JSON.parse(message.body);
              console.log("Notificación parseada:", alert);
              console.log("Driver:", alert.driver);
              console.log("Responsible:", alert.responsible);

              setNotifications((prev) => [alert, ...prev]);

              if (onNotification) {
                onNotification(alert);
              }
            } catch (error) {
              console.error("Error procesando notificación:", error);
            }
          });
        },
        (error: unknown) => {
          console.error("Error en WebSocket:", error);
          setIsConnected(false);

          // Intentar reconectar después de 5 segundos
          setTimeout(() => {
            console.log("Intentando reconectar...");
            connect();
          }, 5000);
        }
      );

      stompClientRef.current = stompClient;
    } catch (error) {
      console.error("Error al conectar WebSocket:", error);
      setIsConnected(false);
    }
  };

  const disconnect = () => {
    if (stompClientRef.current?.connected) {
      stompClientRef.current.disconnect(() => {
        console.log("WebSocket desconectado");
        setIsConnected(false);
      });
    }
  };

  const clearNotifications = () => {
    setNotifications([]);
  };

  useEffect(() => {
    if (autoConnect) {
      connect();
    }

    return () => {
      disconnect();
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return {
    isConnected,
    notifications,
    connect,
    disconnect,
    clearNotifications,
  };
};
