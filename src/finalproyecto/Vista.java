package finalproyecto;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class Vista {
    Frame ventana = new Frame("Taquilla Plaza de Toros");

    // Botones de navegación
    Button btnVerEventosMenu = new Button("Ver Eventos");
    Button btnSacarTicketMenu = new Button("Sacar Ticket");
    Button btnVolverDeFormulario = new Button("Volver al Menú"); 
    Button btnVolverDeEventos = new Button("Volver al Menú"); 
    
    // Botones de acción
    Button btnVerEventosFormulario = new Button("Ver Eventos Disponibles");
    Button btnComprar = new Button("Comprar");

    // Campos de entrada de datos modificados para usar Choice
    TextField txtNombre = new TextField(20);
    TextField txtDni = new TextField(20);
    Choice choiceEvento = new Choice(); // NUEVO
    Choice choiceTipo = new Choice();   // NUEVO
    TextField txtCantidad = new TextField(5);
    
    // Área de visualización de información
    TextArea areaTexto = new TextArea(10, 40);

    // Contenedores principales
    Panel panelMenu = new Panel();
    Panel panelFormulario = new Panel();
    Panel panelEventos = new Panel();

    public Vista() {
        ventana.setLayout(new BorderLayout());

        configurarPanelMenu();
        configurarPanelFormulario();
        configurarPanelEventos();

        // Configuración inicial de la ventana
        areaTexto.setEditable(false);
        ventana.add(areaTexto, BorderLayout.SOUTH);
        mostrarPanelMenu();

        ventana.setSize(400, 450);
        ventana.setResizable(false);
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
    }

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
        camposGrid.add(new Label("Selecciona Evento:")); camposGrid.add(choiceEvento); // NUEVO
        camposGrid.add(new Label("Selecciona Entrada:")); camposGrid.add(choiceTipo);  // NUEVO
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

    public boolean mostrarDialogoConfirmacion(String datosResumen) {
        final Dialog dialogo = new Dialog(ventana, "Confirmar Compra", true);
        dialogo.setLayout(new BorderLayout());
        
        TextArea txtResumen = new TextArea(datosResumen, 8, 35, TextArea.SCROLLBARS_NONE);
        txtResumen.setEditable(false);
        dialogo.add(txtResumen, BorderLayout.CENTER);
        
        Panel panelBotones = new Panel();
        Button btnConfirmar = new Button("Confirmar");
        Button btnCancelar = new Button("Cancelar");
        panelBotones.add(btnConfirmar);
        panelBotones.add(btnCancelar);
        dialogo.add(panelBotones, BorderLayout.SOUTH);
        
        final boolean[] resultadoConfirmacion = {false};
        
        btnConfirmar.addActionListener(e -> {
            resultadoConfirmacion[0] = true;
            dialogo.dispose(); 
        });
        
        btnCancelar.addActionListener(e -> {
            resultadoConfirmacion[0] = false;
            dialogo.dispose();
        });
        
        dialogo.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                resultadoConfirmacion[0] = false;
                dialogo.dispose();
            }
        });
        
        dialogo.setSize(380, 280);
        dialogo.setLocationRelativeTo(ventana); 
        dialogo.setVisible(true); 
        
        return resultadoConfirmacion[0];
    }

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

    // --- GETTERS MODIFICADOS ---

    public String getNombre() { return txtNombre.getText(); }
    public String getDni() { return txtDni.getText(); }
    public String getEvento() { return choiceEvento.getSelectedItem(); } // Saca el texto del choice
    public String getTipo() { return choiceTipo.getSelectedItem(); }     // Saca el texto del choice
    public String getCantidad() { return txtCantidad.getText(); }

    // --- MÉTODOS PARA RELLENAR LOS CHOICE ---
    
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
        // Devolvemos los desplegables a la primera opción
        if (choiceEvento.getItemCount() > 0) choiceEvento.select(0);
        if (choiceTipo.getItemCount() > 0) choiceTipo.select(0);
    }
}