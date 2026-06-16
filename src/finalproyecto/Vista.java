package finalproyecto;

import java.awt.*;
import java.util.List;

// Se encarga EXCLUSIVAMENTE de dibujar la interfaz (no tiene lógica ni eventos)
public class Vista {
    // Ventana principal
    Frame ventana = new Frame("Taquilla Plaza de Toros");

    // Botones de la aplicación
    Button btnVerEventosMenu = new Button("Ver Eventos");
    Button btnSacarTicketMenu = new Button("Sacar Ticket");
    Button btnVolverDeFormulario = new Button("Volver al Menú"); 
    Button btnVolverDeEventos = new Button("Volver al Menú"); 
    Button btnVerEventosFormulario = new Button("Ver Eventos Disponibles");
    Button btnComprar = new Button("Comprar");

    // Componentes de la ventana emergente (Diálogo de confirmación)
    Dialog dialogoConfirmacion;
    TextArea txtResumen = new TextArea("", 8, 35, TextArea.SCROLLBARS_NONE);    
    Button btnConfirmarDialogo = new Button("Confirmar");
    Button btnCancelarDialogo = new Button("Cancelar");

    // Cajas de texto y menús desplegables (Formulario)
    TextField txtNombre = new TextField(20);
    TextField txtDni = new TextField(20);
    Choice choiceEvento = new Choice(); 
    Choice choiceTipo = new Choice();   
    TextField txtCantidad = new TextField(5);
    
    // Caja de texto inferior (consola de mensajes)
    TextArea areaTexto = new TextArea(10, 40);

    // Paneles (cajas invisibles para organizar la pantalla)
    Panel panelMenu = new Panel();
    Panel panelFormulario = new Panel();
    Panel panelEventos = new Panel();

    // Al arrancar, construye todas las pantallas
    public Vista() {
        ventana.setLayout(new BorderLayout());

        configurarPanelMenu();
        configurarPanelFormulario();
        configurarPanelEventos();
        configurarDialogo();

        areaTexto.setEditable(false);
        ventana.add(areaTexto, BorderLayout.SOUTH);
        mostrarPanelMenu();

        ventana.setSize(400, 450);
        ventana.setResizable(false);
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
    }

    // --- MÉTODOS DE DISEÑO ---
    
    private void configurarPanelMenu() {
        panelMenu.setLayout(new GridLayout(2, 1, 10, 10));
        panelMenu.add(btnVerEventosMenu);
        panelMenu.add(btnSacarTicketMenu);
    }

    private void configurarPanelFormulario() {
        panelFormulario.setLayout(new BorderLayout());
        Panel camposGrid = new Panel(new GridLayout(6, 2));
        camposGrid.add(new Label("Nombre Cliente:")); camposGrid.add(txtNombre);
        camposGrid.add(new Label("DNI / Email:")); camposGrid.add(txtDni);
        camposGrid.add(new Label("Selecciona Evento:")); camposGrid.add(choiceEvento);
        camposGrid.add(new Label("Selecciona Entrada:")); camposGrid.add(choiceTipo); 
        camposGrid.add(new Label("Cantidad:")); camposGrid.add(txtCantidad);
        camposGrid.add(btnVolverDeFormulario); 
        camposGrid.add(btnComprar); 
        
        panelFormulario.add(btnVerEventosFormulario, BorderLayout.NORTH);
        panelFormulario.add(camposGrid, BorderLayout.CENTER);
    }

    private void configurarPanelEventos() {
        panelEventos.setLayout(new BorderLayout());
        panelEventos.add(btnVolverDeEventos, BorderLayout.SOUTH);
    }

    private void configurarDialogo() {
        dialogoConfirmacion = new Dialog(ventana, "Confirmar Compra", true);
        dialogoConfirmacion.setLayout(new BorderLayout());
        
        txtResumen.setEditable(false);
        dialogoConfirmacion.add(txtResumen, BorderLayout.CENTER);
        
        Panel panelBotones = new Panel();
        panelBotones.add(btnConfirmarDialogo);
        panelBotones.add(btnCancelarDialogo);
        dialogoConfirmacion.add(panelBotones, BorderLayout.SOUTH);
        
        dialogoConfirmacion.setSize(380, 280);
    }

    // --- MÉTODOS PARA CAMBIAR QUÉ SE VE EN PANTALLA ---

    public void mostrarPanelMenu() {
        ventana.removeAll();
        ventana.add(panelMenu, BorderLayout.CENTER);
        ventana.add(areaTexto, BorderLayout.SOUTH);
        areaTexto.setText("");
        refrescarVentana();
    }

    public void mostrarPanelEventos() {
        ventana.removeAll();
        panelEventos.add(areaTexto, BorderLayout.CENTER);
        ventana.add(panelEventos, BorderLayout.CENTER);
        refrescarVentana();
    }

    public void mostrarPanelFormulario() {
        ventana.removeAll();
        ventana.add(panelFormulario, BorderLayout.CENTER);
        ventana.add(areaTexto, BorderLayout.SOUTH);
        areaTexto.setText("");
        refrescarVentana();
    }

    private void refrescarVentana() {
        ventana.validate();
        ventana.repaint();
    }

    // --- MÉTODOS PARA CONTROLAR LA VENTANA EMERGENTE ---
    
    public void abrirDialogo(String resumen) {
        txtResumen.setText(resumen);
        dialogoConfirmacion.setLocationRelativeTo(ventana);
        dialogoConfirmacion.setVisible(true);
    }

    public void cerrarDialogo() {
        dialogoConfirmacion.setVisible(false);
    }

    // --- MÉTODOS PARA LEER/ESCRIBIR DATOS EN LA INTERFAZ ---

    public String getNombre() { return txtNombre.getText(); }
    public String getDni() { return txtDni.getText(); }
    public String getEvento() { return choiceEvento.getSelectedItem(); } 
    public String getTipo() { return choiceTipo.getSelectedItem(); }     
    public String getCantidad() { return txtCantidad.getText(); }

    public void cargarOpcionesEvento(List<String> eventos) {
        choiceEvento.removeAll(); 
        for (String evento : eventos) {
            choiceEvento.add(evento);
        }
    }

    public void cargarOpcionesTipo(List<String> tipos) {
        choiceTipo.removeAll(); 
        for (String tipo : tipos) {
            choiceTipo.add(tipo);
        }
    }

    public void mostrarMensaje(String mensaje) {
        areaTexto.setText(mensaje);
    }

    public void limpiarFormulario() {
        txtNombre.setText("");
        txtDni.setText("");
        txtCantidad.setText("");
        if (choiceEvento.getItemCount() > 0) choiceEvento.select(0);
        if (choiceTipo.getItemCount() > 0) choiceTipo.select(0);
    }
}