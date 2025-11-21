import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { AlertDialog, AlertDialogContent, AlertDialogHeader, AlertDialogTitle, AlertDialogTrigger } from "@/components/ui/alert-dialog";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Textarea } from "@/components/ui/textarea";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import { Map, Bell, BookOpen, User, Edit, Trash2, Plus } from "lucide-react";
import { useToast } from "@/hooks/use-toast";

interface Alert {
  id: string;
  name: string;
  description: string;
  priority: string;
  area: string;
}

interface PriorityLevel {
  id: number;
  nombre: string;
}

const API_URL = "/api";

const Dashboard = () => {
  const [alerts, setAlerts] = useState<Alert[]>([]);
  const [priorities, setPriorities] = useState<PriorityLevel[]>([]);
  const [newAlert, setNewAlert] = useState({
    name: "",
    description: "",
    priority: "",
    area: "",
  });

  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const { toast } = useToast();

  useEffect(() => {
    const fetchPriorities = async () => {
      try {
        const token = localStorage.getItem("jwt_token") || sessionStorage.getItem("jwt_token");
        if (!token) return;

        const query = `
          query GetAllNivelesPrioridad {
            nivelesPrioridad {
              id
              nombre
            }
          }
        `;

        const response = await fetch(`${API_URL}/alerts/graphql`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({ query }),
        });

        const result = await response.json();
        if (result.data?.nivelesPrioridad) {
          setPriorities(result.data.nivelesPrioridad);
        }
      } catch (error) {
        console.error("Error fetching priorities:", error);
      }
    };

    fetchPriorities();
  }, []);

  const getPriorityId = (priority: string): number => {
    // Si la prioridad es un número (ID), devolverlo
    const id = parseInt(priority);
    if (!isNaN(id)) return id;
    
    // Fallback para compatibilidad
    switch (priority) {
      case "critical": return 1;
      case "high": return 5;
      case "medium": return 6;
      default: return 7;
    }
  };

const handleAddAlert = async () => {
  if (!newAlert.name || !newAlert.description || !newAlert.priority || !newAlert.area) {
    toast({
      title: "Error",
      description: "Por favor completa todos los campos",
      variant: "destructive",
    });
    return;
  }

  try {
    const token =
      localStorage.getItem("jwt_token") || sessionStorage.getItem("jwt_token");

    if (!token) {
      toast({
        title: "Error",
        description: "No se encontró el token de autenticación. Inicia sesión nuevamente.",
        variant: "destructive",
      });
      return;
    }

    const mutation = `
      mutation CreateTipoAlerta($input: TipoAlertaCreateInput!) {
        createTipoAlerta(input: $input) {
          id
          nombre
          descripcion
          nivelPrioridad {
            id
            nombre
          }
          tipoEncargado
        }
      }
    `;

    const variables = {
      input: {
        nombre: newAlert.name,
        descripcion: newAlert.description,
        nivelPrioridadId: getPriorityId(newAlert.priority),
        tipoEncargado: newAlert.area,
      },
    };

    const response = await fetch(`${API_URL}/alerts/graphql`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({ query: mutation, variables }),
    });

    const result = await response.json();

    if (result.errors) {
      throw new Error(result.errors[0]?.message || "Error al crear la alerta");
    }

    const created = result.data.createTipoAlerta;

    const alert: Alert = {
      id: created.id.toString(),
      name: created.nombre,
      description: created.descripcion,
      priority:
        created.nivelPrioridad.nombre === "Crítica"
          ? "critical"
          : created.nivelPrioridad.nombre === "Alta"
          ? "high"
          : "medium",
      area: created.tipoEncargado,
    };

    setAlerts((prev) => [...prev, alert]);
    setNewAlert({ name: "", description: "", priority: "", area: "" });
    setIsDialogOpen(false);

    toast({
      title: "Alerta creada",
      description: `Se ha creado la alerta "${alert.name}" exitosamente`,
    });
  } catch (error: unknown) {
    const message =
      error instanceof Error ? error.message : "No se pudo crear la alerta";
    toast({
      title: "Error al crear la alerta",
      description: message,
      variant: "destructive",
    });
  }
};


  const handleDeleteAlert = (id: string) => {
    setAlerts(alerts.filter(alert => alert.id !== id));
    toast({
      title: "Alerta eliminada",
      description: "La alerta ha sido eliminada del sistema"
    });
  };

  const getPriorityColor = (priority: string) => {
    switch (priority) {
      case "critical": return "bg-priority-critical text-white";
      case "high": return "bg-priority-high text-white";
      case "medium": return "bg-priority-medium text-white";
      default: return "bg-muted";
    }
  };

  const getPriorityText = (priority: string) => {
    switch (priority) {
      case "critical": return "Crítica";
      case "high": return "Alta";
      case "medium": return "Media";
      default: return priority;
    }
  };

  return (
    <div className="min-h-screen flex w-full bg-background">
      {/* Sidebar */}
      <div className="fixed left-0 top-0 h-screen w-[92px] bg-[#0A2846] flex flex-col items-center pt-5 z-10">
        <div className="flex flex-col space-y-[15px] items-center">
          <Button
            variant="ghost"
            size="icon"
            className="text-white hover:bg-white/10 w-[28px] h-[28px] p-0"
          >
            <Map className="h-[28px] w-[28px]" />
          </Button>
          
          <Button
            variant="ghost"
            size="icon"
            className="text-white hover:bg-white/10 w-[28px] h-[28px] p-0"
            asChild
          >
            <Link to="/dashboard">
              <Bell className="h-[28px] w-[28px]" />
            </Link>
          </Button>
          
          <Button
            variant="ghost"
            size="icon"
            className="text-white hover:bg-white/10 w-[28px] h-[28px] p-0"
            asChild
          >
            <Link to="/panel-de-alertas">
              <BookOpen className="h-[28px] w-[28px]" />
            </Link>
          </Button>
          
          <Button
            variant="ghost"
            size="icon"
            className="text-white hover:bg-white/10 w-[28px] h-[28px] p-0"
          >
            <User className="h-[28px] w-[28px]" />
          </Button>
        </div>
      </div>

      {/* Contenido Principal */}
      <main className="ml-[92px] flex-1 p-6">
          <div className="flex items-center justify-between mb-8">
            <div className="flex items-center space-x-4">
              <h1 className="text-3xl font-bold text-foreground">Alertas Activas</h1>
            </div>
            
            <AlertDialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
              <AlertDialogTrigger asChild>
                <Button 
                  variant="outline" 
                  className="border-2 border-muted text-muted-foreground hover:bg-muted/10"
                >
                  <Plus className="w-4 h-4 mr-2" />
                  ADD ITEM
                </Button>
              </AlertDialogTrigger>
              
              <AlertDialogContent className="max-w-md">
                <AlertDialogHeader>
                  <AlertDialogTitle>Agregar Nueva Alerta</AlertDialogTitle>
                </AlertDialogHeader>
                
                <div className="space-y-4">
                  <div>
                    <Label htmlFor="alert-name">Nombre de la Alerta</Label>
                    <Input
                      id="alert-name"
                      placeholder="Ingresa el nombre de la alerta"
                      value={newAlert.name}
                      onChange={(e) => setNewAlert({ ...newAlert, name: e.target.value })}
                      className="mt-2"
                    />
                  </div>
                  
                  <div>
                    <Label htmlFor="alert-description">Descripción Corta</Label>
                    <Textarea
                      id="alert-description"
                      placeholder="Describe la alerta brevemente"
                      value={newAlert.description}
                      onChange={(e) => setNewAlert({ ...newAlert, description: e.target.value })}
                      className="mt-2"
                    />
                  </div>
                  
                  <div>
                    <Label>Seleccionar Área Encargada</Label>
                    <Select 
                      value={newAlert.area} 
                      onValueChange={(value) => setNewAlert({ ...newAlert, area: value })}
                    >
                      <SelectTrigger className="mt-2">
                        <SelectValue placeholder="Selecciona un área" />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value="MECANICO">Mecánico</SelectItem>
                        <SelectItem value="SEGURIDAD">Seguridad</SelectItem>
                        <SelectItem value="OPERADOR_LOGISTICA">Operador Logística</SelectItem>
                        <SelectItem value="SOPORTE_TECNICO">Soporte Técnico</SelectItem>
                      </SelectContent>
                    </Select>
                  </div>
                  
                  <div>
                    <Label>Seleccionar Prioridad</Label>
                    <Select 
                      value={newAlert.priority} 
                      onValueChange={(value) => setNewAlert({ ...newAlert, priority: value })}
                    >
                      <SelectTrigger className="mt-2">
                        <SelectValue placeholder="Selecciona una prioridad" />
                      </SelectTrigger>
                      <SelectContent>
                        {priorities.map((priority) => (
                          <SelectItem key={priority.id} value={priority.id.toString()}>
                            {priority.nombre}
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                  </div>
                  
                  <div className="flex space-x-2 pt-4">
                    <Button onClick={handleAddAlert} className="flex-1">
                      Agregar Alerta
                    </Button>
                    <Button 
                      variant="outline" 
                      onClick={() => setIsDialogOpen(false)}
                      className="flex-1"
                    >
                      Cancelar
                    </Button>
                  </div>
                </div>
              </AlertDialogContent>
            </AlertDialog>
          </div>

          {/* Tabla de Alertas */}
          <Card>
            <CardContent className="p-0">
              <Table>
                <TableHeader>
                  <TableRow className="bg-muted/50">
                    <TableHead className="font-bold text-foreground">Nombre</TableHead>
                    <TableHead className="font-bold text-foreground">Descripción</TableHead>
                    <TableHead className="font-bold text-foreground">Prioridad</TableHead>
                    <TableHead className="font-bold text-foreground">Área Encargada</TableHead>
                    <TableHead className="font-bold text-foreground">Acciones</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {alerts.map((alert, index) => (
                    <TableRow 
                      key={alert.id} 
                      className={index % 2 === 1 ? "bg-light-aqua" : "bg-white"}
                    >
                      <TableCell className="font-medium">{alert.name}</TableCell>
                      <TableCell>{alert.description}</TableCell>
                      <TableCell>
                        <Badge className={getPriorityColor(alert.priority)}>
                          {getPriorityText(alert.priority)}
                        </Badge>
                      </TableCell>
                      <TableCell>{alert.area}</TableCell>
                      <TableCell>
                        <div className="flex space-x-2">
                          <Button size="sm" variant="ghost" className="text-primary hover:bg-primary/10">
                            <Edit className="h-4 w-4" />
                          </Button>
                          <Button 
                            size="sm" 
                            variant="ghost" 
                            className="text-destructive hover:bg-destructive/10"
                            onClick={() => handleDeleteAlert(alert.id)}
                          >
                            <Trash2 className="h-4 w-4" />
                          </Button>
                        </div>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </CardContent>
          </Card>
        </main>
      </div>
  );
};

export default Dashboard;
