import { useState, useEffect } from "react";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Input } from "@/components/ui/input";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";

import { Search, RefreshCw, History as HistoryIcon } from "lucide-react";
import { Sidebar } from "@/components/Sidebar";
import { useGraphQL } from "@/hooks/use-graphql";
import { useToast } from "@/hooks/use-toast";

interface AlertResponse {
  id: string;
  alertType: string;
  responsible: string;
  priority: string;
  driver: string;
  generatingUnit: string;
  state: string;
  generationDate: string;
}

interface Alert {
  id: string;
  tipo: string;
  responsable: string;
  prioridad: string;
  unidad: string;
  conductor: string;
  generado: string;
  estado: string;
}

const PanelDeAlertas = () => {
  const [alerts, setAlerts] = useState<Alert[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const { fetchGraphQL } = useGraphQL();
  const { toast } = useToast();

  useEffect(() => {
    fetchAlerts();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const fetchAlerts = async () => {
    try {
      setIsLoading(true);
      const query = `
        query GetAllAlerts {
          getAllAlerts {
            id
            alertType
            responsible
            priority
            driver
            generatingUnit
            state
            generationDate
          }
        }
      `;

      const result = await fetchGraphQL<{ getAllAlerts: AlertResponse[] }>(
        "/panel/graphql",
        query
      );

      const transformedAlerts: Alert[] = result.getAllAlerts.map((alert) => ({
        id: alert.id,
        tipo: alert.alertType,
        responsable: alert.responsible,
        prioridad: alert.priority,
        unidad: alert.generatingUnit,
        conductor: alert.driver,
        generado: alert.generationDate,
        estado: alert.state,
      }));

      console.log("Alertas recibidas del backend:", transformedAlerts);
      console.log("Estados únicos:", [...new Set(transformedAlerts.map(a => a.estado))]);
      console.log("Prioridades únicas:", [...new Set(transformedAlerts.map(a => a.prioridad))]);
      
      setAlerts(transformedAlerts);
    } catch (error) {
      console.error("Error fetching alerts:", error);
      toast({
        title: "Error",
        description: "No se pudieron cargar las alertas del historial",
        variant: "destructive",
      });
    } finally {
      setIsLoading(false);
    }
  };



  const getPriorityColor = (prioridad: string) => {
    const prioridadLower = prioridad.toLowerCase().trim();
    if (prioridadLower.includes("alta") || prioridadLower === "high") {
      return "bg-red-600 text-white font-semibold";
    }
    if (prioridadLower.includes("media") || prioridadLower === "medium") {
      return "bg-yellow-500 text-gray-900 font-semibold";
    }
    if (prioridadLower.includes("baja") || prioridadLower === "low") {
      return "bg-green-600 text-white font-semibold";
    }
    return "bg-gray-500 text-white";
  };

  const formatDate = (isoDate: string) => {
    try {
      const date = new Date(isoDate);
      const day = date.getDate().toString().padStart(2, '0');
      const month = (date.getMonth() + 1).toString().padStart(2, '0');
      const year = date.getFullYear();
      const hours = date.getHours().toString().padStart(2, '0');
      const minutes = date.getMinutes().toString().padStart(2, '0');
      
      return `${day}/${month}/${year} ${hours}:${minutes}`;
    } catch (error) {
      console.error("Error formateando fecha:", error);
      return isoDate;
    }
  };

  const getEstadoColor = (estado: string) => {
    const estadoLower = estado.toLowerCase().trim();
    
    // Debug: log del estado recibido
    console.log(`Estado recibido: "${estado}" -> normalizado: "${estadoLower}"`);
    
    if (estadoLower.includes("espera") || estadoLower === "pending" || estadoLower === "en espera") {
      return "bg-yellow-500 text-gray-900 font-semibold";
    }
    if (estadoLower.includes("proceso") || estadoLower === "in progress" || estadoLower === "en proceso") {
      return "bg-blue-600 text-white font-semibold";
    }
    if (estadoLower.includes("resuelto") || estadoLower === "resolved") {
      return "bg-green-600 text-white font-semibold";
    }
    
    // Si no coincide con ninguno, log de advertencia
    console.warn(`Estado no reconocido: "${estado}"`);
    return "bg-gray-500 text-white";
  };

  return (
    <div className="min-h-screen flex w-full bg-[#0E1525]">
      <Sidebar />

      {/* Contenido Principal */}
      <main className="ml-[240px] flex-1 p-6">
        {/* Encabezado */}
        <div className="bg-[#0A2846] rounded-lg px-6 py-4 mb-6 flex items-center gap-3">
          <HistoryIcon className="h-8 w-8 text-[#3B82F6]" />
          <h1 className="text-2xl font-semibold text-white tracking-wide ">
            Historial de Alertas
          </h1>
        </div>

        {/* Barra de filtros */}
        <Card className="bg-[#141C2F] border-none mb-6">
          <CardContent className="p-4">
            <div className="flex flex-wrap items-center gap-3">
              {/* Filtros */}
              <Select>
                <SelectTrigger className="w-[140px] bg-[#0E1525] border-gray-700 text-[#E5E7EB]">
                  <SelectValue placeholder="Tipo" />
                </SelectTrigger>
                <SelectContent className="bg-[#141C2F] border-gray-700">
                  <SelectItem value="velocidad">Exceso de velocidad</SelectItem>
                  <SelectItem value="frenado">Frenado brusco</SelectItem>
                  <SelectItem value="geocerca">Entrada a geocerca</SelectItem>
                  <SelectItem value="motor">Motor apagado</SelectItem>
                  <SelectItem value="bateria">Batería baja</SelectItem>
                </SelectContent>
              </Select>

              <Select>
                <SelectTrigger className="w-[160px] bg-[#0E1525] border-gray-700 text-[#E5E7EB]">
                  <SelectValue placeholder="Responsable" />
                </SelectTrigger>
                <SelectContent className="bg-[#141C2F] border-gray-700">
                  <SelectItem value="monitoreo">Área de monitoreo</SelectItem>
                  <SelectItem value="mantenimiento">Mantenimiento</SelectItem>
                  <SelectItem value="logistica">Logística</SelectItem>
                  <SelectItem value="taller">Taller mecánico</SelectItem>
                </SelectContent>
              </Select>

              <Select>
                <SelectTrigger className="w-[140px] bg-[#0E1525] border-gray-700 text-[#E5E7EB]">
                  <SelectValue placeholder="Prioridad" />
                </SelectTrigger>
                <SelectContent className="bg-[#141C2F] border-gray-700">
                  <SelectItem value="alta">Alta</SelectItem>
                  <SelectItem value="media">Media</SelectItem>
                  <SelectItem value="baja">Baja</SelectItem>
                </SelectContent>
              </Select>

              <Select>
                <SelectTrigger className="w-[140px] bg-[#0E1525] border-gray-700 text-[#E5E7EB]">
                  <SelectValue placeholder="Unidad" />
                </SelectTrigger>
                <SelectContent className="bg-[#141C2F] border-gray-700">
                  <SelectItem value="camion-123">Camión-123</SelectItem>
                  <SelectItem value="furgoneta-456">Furgoneta-456</SelectItem>
                  <SelectItem value="coche-789">Coche-789</SelectItem>
                  <SelectItem value="autobus-101">Autobús-101</SelectItem>
                  <SelectItem value="camion-234">Camión-234</SelectItem>
                </SelectContent>
              </Select>

              <Select>
                <SelectTrigger className="w-[160px] bg-[#0E1525] border-gray-700 text-[#E5E7EB]">
                  <SelectValue placeholder="Conductor" />
                </SelectTrigger>
                <SelectContent className="bg-[#141C2F] border-gray-700">
                  <SelectItem value="ana">Ana Gómez</SelectItem>
                  <SelectItem value="carlos">Carlos Sanchez</SelectItem>
                  <SelectItem value="laura">Laura Fernandez</SelectItem>
                  <SelectItem value="david">David Lopez</SelectItem>
                  <SelectItem value="sara">Sara Moreno</SelectItem>
                </SelectContent>
              </Select>

              <Select>
                <SelectTrigger className="w-[140px] bg-[#0E1525] border-gray-700 text-[#E5E7EB]">
                  <SelectValue placeholder="Estado" />
                </SelectTrigger>
                <SelectContent className="bg-[#141C2F] border-gray-700">
                  <SelectItem value="espera">En espera</SelectItem>
                  <SelectItem value="proceso">En proceso</SelectItem>
                  <SelectItem value="resuelto">Resuelto</SelectItem>
                </SelectContent>
              </Select>

              <div className="flex-1" />

              {/* Búsqueda y Recarga */}
              <div className="flex items-center gap-2">
                <div className="relative">
                  <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-400" />
                  <Input
                    placeholder="Buscar"
                    className="pl-10 w-[200px] bg-[#0E1525] border-gray-700 text-[#E5E7EB]"
                  />
                </div>
                <Button 
                  variant="ghost" 
                  size="icon" 
                  className="text-[#E5E7EB] hover:bg-[#0E1525]"
                  onClick={fetchAlerts}
                  disabled={isLoading}
                >
                  <RefreshCw className={`h-5 w-5 ${isLoading ? 'animate-spin' : ''}`} />
                </Button>
              </div>
            </div>
          </CardContent>
        </Card>

        {/* Tabla de Alertas */}
        <Card className="bg-[#141C2F] border-none">
          <CardContent className="p-0">
            <Table>
              <TableHeader>
                <TableRow className="border-gray-700 hover:bg-transparent">
                  <TableHead className="font-semibold text-[#E5E7EB] h-12">Tipo</TableHead>
                  <TableHead className="font-semibold text-[#E5E7EB]">Responsable</TableHead>
                  <TableHead className="font-semibold text-[#E5E7EB]">Prioridad</TableHead>
                  <TableHead className="font-semibold text-[#E5E7EB]">Unidad</TableHead>
                  <TableHead className="font-semibold text-[#E5E7EB]">Conductor</TableHead>
                  <TableHead className="font-semibold text-[#E5E7EB]">Generado</TableHead>
                  <TableHead className="font-semibold text-[#E5E7EB]">Estado</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {isLoading ? (
                  <TableRow>
                    <TableCell colSpan={7} className="text-center text-[#E5E7EB] py-8">
                      <div className="flex items-center justify-center gap-2">
                        <RefreshCw className="h-5 w-5 animate-spin" />
                        <span>Cargando historial de alertas...</span>
                      </div>
                    </TableCell>
                  </TableRow>
                ) : alerts.length === 0 ? (
                  <TableRow>
                    <TableCell colSpan={7} className="text-center text-[#E5E7EB] py-8">
                      No hay alertas en el historial
                    </TableCell>
                  </TableRow>
                ) : (
                  alerts.map((alert, index) => (
                    <TableRow 
                      key={alert.id} 
                      className={`border-gray-700 ${
                        index % 2 === 0 ? "bg-[#131A2C]" : "bg-[#101725]"
                      }`}
                    >
                      <TableCell className="text-[#E5E7EB]">{alert.tipo}</TableCell>
                      <TableCell className="text-[#E5E7EB]">{alert.responsable}</TableCell>
                      <TableCell>
                        <Badge className={`${getPriorityColor(alert.prioridad)} rounded-full px-4 py-1.5 text-sm`}>
                          {alert.prioridad}
                        </Badge>
                      </TableCell>
                      <TableCell className="text-[#E5E7EB]">{alert.unidad}</TableCell>
                      <TableCell className="text-[#E5E7EB]">{alert.conductor}</TableCell>
                      <TableCell className="text-[#E5E7EB] text-sm">{formatDate(alert.generado)}</TableCell>
                      <TableCell>
                        <Badge className={`${getEstadoColor(alert.estado)} rounded-full px-4 py-1.5 text-sm`}>
                          {alert.estado}
                        </Badge>
                      </TableCell>
                    </TableRow>
                  ))
                )}
              </TableBody>
            </Table>
          </CardContent>
        </Card>
      </main>
    </div>
  );
};

export default PanelDeAlertas;
