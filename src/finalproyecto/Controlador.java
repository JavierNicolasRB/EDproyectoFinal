package finalproyecto;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.Button;

public class Controlador implements ActionListener, WindowListener {
    private Vista vista;
    private Modelo modelo;

    public Controlador(Vista vista, Modelo modelo) {
        this.vista = vista;
        this.modelo = modelo;

        this.vista.btnVerEventosMenu.addActionListener(this);
        this.vista.btnSacarTicketMenu.addActionListener(this);
        this.vista.btnVolverDeFormulario.addActionListener(this);
        this.vista.btnVolverDeEventos.addActionListener(this);
        this.vista.btnVerEventosFormulario.addActionListener(this);
        this.vista.btnComprar.addActionListener(this);
        this.vista.ventana.addWindowListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String botonPulsado = ((Button) e.getSource()).getLabel();

        if (botonPulsado.equals("Ver Eventos")) {
            vista.mostrarPanelEventos();
            cargarYMostrarEventos();
        } 
        else if (botonPulsado.equals("Sacar Ticket")) {
            vista.mostrarPanelFormulario();
        } 
        else if (botonPulsado.equals("Volver al Menú")) {
            vista.mostrarPanelMenu();
        }
        else if (botonPulsado.equals("Ver Eventos Disponibles")) {
            cargarYMostrarEventos();
        } 
        else if (botonPulsado.equals("Comprar")) {
            procesarCompra();
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
            int idEvento = Integer.parseInt(vista.getEvento());
            int idTipo = Integer.parseInt(vista.getTipo());
            int cantidad = Integer.parseInt(vista.getCantidad());

            // --- CAMBIADO: Recibimos todos los datos del tipo de entrada ---
            String[] datosTipo = modelo.obtenerDatosTipo(idTipo);
            double precioBase = Double.parseDouble(datosTipo[0]);
            String nombreTipo = datosTipo[1];
            String descTipo = datosTipo[2];
            
            double total = cantidad * precioBase;

            // --- CAMBIADO: Añadimos la descripción al resumen ---
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

            boolean veredictoUsuario = vista.mostrarDialogoConfirmacion(resumenCompra);

            if (veredictoUsuario) {
                modelo.registrarVenta(nombre, dniEmail, cantidad, total, idEvento, idTipo);
                vista.mostrarMensaje("¡COMPRA CORRECTA!\n\nSe ha guardado el ticket de " + nombre + " por un total de " + total + "€.");
                vista.limpiarFormulario();
            } else {
                vista.mostrarMensaje("Operación cancelada. No se ha realizado ningún cargo.");
            }

        } catch (NumberFormatException ex) {
            vista.mostrarMensaje("Error: Por favor, introduce números válidos en ID Evento, Tipo y Cantidad.");
        } catch (Exception ex) {
            vista.mostrarMensaje("Error en la operación: " + ex.getMessage());
        }
    }

    public void windowClosing(WindowEvent e) { System.exit(0); }
    public void windowOpened(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
}