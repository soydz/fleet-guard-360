import { Button } from "@/components/ui/button";
import { Download, BarChart3 } from "lucide-react";
import { Sidebar } from "@/components/Sidebar";
import { useToast } from "@/hooks/use-toast";
import { useState } from "react";

const Estadisticas = () => {
  const { toast } = useToast();
  const [isExporting, setIsExporting] = useState(false);

  const handleExportCSV = async () => {
    setIsExporting(true);
    try {
      // TODO: Implementar endpoint de descarga
      toast({
        title: "Exportando datos",
        description: "La descarga comenzará en breve...",
      });
      
      // Aquí irá la llamada al endpoint de exportación
      // const response = await fetch('/api/export-csv', ...);
      
    } catch (error) {
      console.error("Error exportando CSV:", error);
      toast({
        title: "Error",
        description: "No se pudo exportar el archivo CSV",
        variant: "destructive",
      });
    } finally {
      setIsExporting(false);
    }
  };

  return (
    <div className="min-h-screen flex w-full bg-[#0E1525]">
      <Sidebar />

      {/* Contenido Principal */}
      <main className="ml-[240px] flex-1 p-6">
        {/* Encabezado */}
        <div className="bg-[#0A2846] rounded-lg px-6 py-4 mb-6 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <BarChart3 className="h-8 w-8 text-[#3B82F6]" />
            <h1 className="text-2xl font-semibold text-white tracking-wide uppercase">
              Tablero de Alertas Recurrentes
            </h1>
          </div>
          
          <Button 
            onClick={handleExportCSV}
            disabled={isExporting}
            className="bg-[#3B82F6] hover:bg-[#2563EB] text-white font-medium px-6 gap-2"
          >
            <Download className="h-5 w-5" />
            {isExporting ? "Exportando..." : "Exportar a CSV"}
          </Button>
        </div>

        {/* Power BI Dashboard */}
        <div className="bg-[#141C2F] rounded-lg overflow-hidden shadow-lg">
          <iframe 
            title="Informe Fabrica Escuela 2025-2 Alertas" 
            width="100%" 
            height="900" 
            src="https://app.powerbi.com/view?r=eyJrIjoiNDAxZGE3OTUtYjliNi00MjYyLTlmNmItMTA4YTY2NzVmZGQxIiwidCI6Ijk5ZTFlNzIxLTcxODQtNDk4ZS04YWZmLWIyYWQ0ZTUzYzFjMiIsImMiOjR9" 
            frameBorder="0" 
            allowFullScreen={true}
            className="w-full"
          />
        </div>
      </main>
    </div>
  );
};

export default Estadisticas;
