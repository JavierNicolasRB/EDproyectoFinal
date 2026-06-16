package finalproyecto;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Button;

public class Controlador implements ActionListener {
    private Vista vista;
    private Modelo modelo;

    public Controlador(Vista vista, Modelo modelo) {
        this.vista = vista;
        this.modelo = modelo;

        // Registro de eventos para los botones
        this.vista.btnVerEventosMenu.addActionListener(this);
        this.vista.btnSacarTicketMenu.addActionListener(this);
        this.vista.btnVolverDeFormulario.addActionListener(this);
        this.vista.btnVolverDeEventos.addActionListener(this);
        this.vista.btnVerEventosFormulario.addActionListener(this);
        this.vista.btnComprar.addActionListener(this);
        
        // Cierra la aplicación de forma limpia al pulsar la 'X'
        this.vista.ventana.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        inicializarDesplegables();
    }

    private void inicializarDesplegables() {
        vista.cargarOpcionesEvento(modelo.obtenerListaEventos());
        vista.cargarOpcionesTipo(modelo.obtenerListaTipos());
    }

    
    @Override
    public void actionPerformed(ActionEvent e) {
        String botonPulsado = ((Button) e.getSource()).getLabel();

        switch (botonPulsado) {
            case "Ver Eventos": 
                // Si pulsa desde el menú principal, sí cambiamos de panel
                vista.mostrarPanelEventos();
                cargarYMostrarEventos();
                break;
                
            case "Ver Eventos Disponibles":
                // Si pulsa desde el formulario, NO cambiamos de panel.
                // Solo cargamos los datos en el área de texto inferior.
                cargarYMostrarEventos();
                break;
                
            case "Sacar Ticket":
                vista.mostrarPanelFormulario();
                break;
                
            case "Volver al Menú":
                vista.mostrarPanelMenu();
                break;
                
            case "Comprar":
                procesarCompra();
                break;
        }
    }

    private void cargarYMostrarEventos() {
        String eventos = modelo.obtenerEventosDisponibles();
        String tipos = modelo.obtenerTiposDisponibles();
        vista.mostrarMensaje(eventos + "\n" + tipos);
    }

    private void procesarCompra() {
        try {
            String nombre = vista.getNombre();
            String dniEmail = vista.getDni();
            int cantidad = Integer.parseInt(vista.getCantidad());

            String textoEvento = vista.getEvento();
            String textoTipo = vista.getTipo();
            
            if (textoEvento == null || textoTipo == null) {
                vista.mostrarMensaje("Error: No hay eventos o tipos seleccionados.");
                return;
            }

            // NUEVO: Extraer solo el número de ID usando split
            int idEvento = Integer.parseInt(textoEvento.split(" - ")[0]);
            int idTipo = Integer.parseInt(textoTipo.split(" - ")[0]);

            // 1. Extraemos los datos cruzados de la BD
            String[] datosTipo = modelo.obtenerDatosTipo(idTipo);
            double precioBase = Double.parseDouble(datosTipo[0]);
            String nombreTipo = datosTipo[1];
            String descTipo = datosTipo[2];
            
            double total = cantidad * precioBase;

            // 2. Preparamos el recibo para validación
            String resumenCompra = "  --- RESUMEN DE SU ORDEN ---\n\n" +
                                   " Cliente: " + nombre + "\n" +
                                   " DNI / Email: " + dniEmail + "\n" +
                                   " ID Evento: " + idEvento + "\n" +
                                   " Tipo: " + nombreTipo + " (" + descTipo + ")\n" +
                                   " Cantidad: " + cantidad + " ticket(s)\n" +
                                   " Precio Unitario: " + precioBase + " €\n" +
                                   "-------------------------------------------\n" +
                                   " TOTAL A PAGAR: " + total + " €\n\n" +
                                   " ¿Desea confirmar el pago?";

            // 3. Mostramos ventana bloqueante y esperamos respuesta
            boolean veredictoUsuario = vista.mostrarDialogoConfirmacion(resumenCompra);

            if (veredictoUsuario) {
                modelo.registrarVenta(nombre, dniEmail, cantidad, total, idEvento, idTipo);
                vista.mostrarMensaje("¡COMPRA CORRECTA!\n\nSe ha guardado el ticket de " + nombre + " por un total de " + total + "€.");
                vista.limpiarFormulario();
            } else {
                vista.mostrarMensaje("Operación cancelada. No se ha realizado ningún cargo.");
            }

        } catch (NumberFormatException ex) {
            vista.mostrarMensaje("Error: Por favor, asegúrate de que la cantidad es un número válido.");
        } catch (Exception ex) {
            vista.mostrarMensaje("Error en la operación: " + ex.getMessage());
        }
    }
}