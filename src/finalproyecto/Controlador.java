package finalproyecto;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Button;

// Es el "Cerebro". Escucha a la Vista y da órdenes al Modelo.
public class Controlador implements ActionListener {
    private Vista vista;
    private Modelo modelo;

    // Variables que guardan los datos del formulario mientras esperamos a que el usuario confirme
    private String nombrePendiente;
    private String dniPendiente;
    private int idEventoPendiente;
    private int idTipoPendiente;
    private int cantidadPendiente;
    private double totalPendiente;

    public Controlador(Vista vista, Modelo modelo) {
        this.vista = vista;
        this.modelo = modelo;

        // Se enchufan los botones de la vista a este controlador
        this.vista.btnVerEventosMenu.addActionListener(this);
        this.vista.btnSacarTicketMenu.addActionListener(this);
        this.vista.btnVolverDeFormulario.addActionListener(this);
        this.vista.btnVolverDeEventos.addActionListener(this);
        this.vista.btnVerEventosFormulario.addActionListener(this);
        this.vista.btnComprar.addActionListener(this);
        this.vista.btnConfirmarDialogo.addActionListener(this);
        this.vista.btnCancelarDialogo.addActionListener(this);
        
        // Configura el cierre del programa al darle a la 'X' principal
        this.vista.ventana.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // Configura el cierre de la ventana emergente al darle a su 'X'
        this.vista.dialogoConfirmacion.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cancelarCompra();
            }
        });

        // Pide los datos al modelo para rellenar los desplegables al arrancar
        inicializarDesplegables();
    }

    private void inicializarDesplegables() {
        vista.cargarOpcionesEvento(modelo.obtenerListaEventos());
        vista.cargarOpcionesTipo(modelo.obtenerListaTipos());
    }

    // Se ejecuta automáticamente cada vez que se pulsa CUALQUIER botón
    @Override
    public void actionPerformed(ActionEvent e) {
        String botonPulsado = ((Button) e.getSource()).getLabel();

        // Decide qué hacer en función del texto del botón pulsado
        switch (botonPulsado) {
            case "Ver Eventos":
                vista.mostrarPanelEventos();
                cargarYMostrarEventos();
                break;
                
            case "Ver Eventos Disponibles":
                cargarYMostrarEventos();
                break;
                
            case "Sacar Ticket":
                vista.mostrarPanelFormulario();
                break;
                
            case "Volver al Menú":
                vista.mostrarPanelMenu();
                break;
                
            case "Comprar":
                prepararCompra();
                break;
                
            case "Confirmar":
                ejecutarCompraFinal();
                break;
                
            case "Cancelar":
                cancelarCompra();
                break;
        }
    }

    // Carga el bloque de texto con toda la info de la BD y lo muestra abajo
    private void cargarYMostrarEventos() {
        String eventos = modelo.obtenerEventosDisponibles();
        String tipos = modelo.obtenerTiposDisponibles();
        vista.mostrarMensaje(eventos + "\n" + tipos);
    }

    // Fase 1 de la compra: Lee, valida, calcula el total y abre la ventana de confirmación
    private void prepararCompra() {
        try {
            nombrePendiente = vista.getNombre();
            dniPendiente = vista.getDni();
            cantidadPendiente = Integer.parseInt(vista.getCantidad());

            String textoEvento = vista.getEvento();
            String textoTipo = vista.getTipo();
            
            if (textoEvento == null || textoTipo == null) {
                vista.mostrarMensaje("Error: No hay eventos o tipos seleccionados.");
                return;
            }

            // Corta el texto (ej: "1 - Concierto") para quedarse solo con el ID ("1")
            idEventoPendiente = Integer.parseInt(textoEvento.split(" - ")[0]);
            idTipoPendiente = Integer.parseInt(textoTipo.split(" - ")[0]);

            // Busca el precio en BD y calcula el total
            String[] datosTipo = modelo.obtenerDatosTipo(idTipoPendiente);
            double precioBase = Double.parseDouble(datosTipo[0]);
            String nombreTipo = datosTipo[1];
            String descTipo = datosTipo[2];
            totalPendiente = cantidadPendiente * precioBase;

            // Construye el ticket resumen
            String resumenCompra = "  --- RESUMEN DE SU ORDEN ---\n\n" +
                                   " Cliente: " + nombrePendiente + "\n" +
                                   " DNI / Email: " + dniPendiente + "\n" +
                                   " ID Evento: " + idEventoPendiente + "\n" +
                                   " Tipo: " + nombreTipo + " (" + descTipo + ")\n" +
                                   " Cantidad: " + cantidadPendiente + " ticket(s)\n" +
                                   " Precio Unitario: " + precioBase + " €\n" +
                                   "-------------------------------------------\n" +
                                   " TOTAL A PAGAR: " + totalPendiente + " €\n\n" +
                                   " ¿Desea confirmar el pago?";

            vista.abrirDialogo(resumenCompra);

        } catch (NumberFormatException ex) {
            vista.mostrarMensaje("Error: Por favor, asegúrate de que la cantidad es un número válido.");
        } catch (Exception ex) {
            vista.mostrarMensaje("Error en la operación: " + ex.getMessage());
        }
    }

    // Fase 2 de la compra: Si el usuario dice "SÍ", manda los datos al modelo (BD)
    private void ejecutarCompraFinal() {
        try {
            modelo.registrarVenta(nombrePendiente, dniPendiente, cantidadPendiente, totalPendiente, idEventoPendiente, idTipoPendiente);
            vista.cerrarDialogo();
            vista.mostrarMensaje("¡COMPRA CORRECTA!\n\nSe ha guardado el ticket de " + nombrePendiente + " por un total de " + totalPendiente + "€.");
            vista.limpiarFormulario();
        } catch (Exception ex) {
            vista.cerrarDialogo();
            vista.mostrarMensaje("Error al guardar en la base de datos: " + ex.getMessage());
        }
    }

    // Se ejecuta si el usuario cancela en la ventana emergente
    private void cancelarCompra() {
        vista.cerrarDialogo();
        vista.mostrarMensaje("Operación cancelada. No se ha realizado ningún cargo.");
    }
}