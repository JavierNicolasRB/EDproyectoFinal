package finalproyecto;

import java.awt.*;

public class Vista {
    Frame ventana = new Frame("Taquilla Plaza de Toros");

    Button btnVerEventosMenu = new Button("Ver Eventos");
    Button btnSacarTicketMenu = new Button("Sacar Ticket");
    
    Button btnVolverDeFormulario = new Button("Volver al Menú"); 
    Button btnVolverDeEventos = new Button("Volver al Menú"); 

    Button btnVerEventosFormulario = new Button("Ver Eventos Disponibles");
    
    // --- CAMBIADO: Ahora el botón solo dice "Comprar" ---
    Button btnComprar = new Button("Comprar");

    TextField txtNombre = new TextField(20);
    TextField txtDni = new TextField(20);
    TextField txtEvento = new TextField(5);
    TextField txtTipo = new TextField(5);
    TextField txtCantidad = new TextField(5);
    TextArea areaTexto = new TextArea(10, 40);

    Panel panelMenu = new Panel();
    Panel panelFormulario = new Panel();
    Panel panelEventos = new Panel();

    public Vista() {
        ventana.setLayout(new BorderLayout());

        panelMenu.setLayout(new GridLayout(2, 1, 10, 10));
        panelMenu.add(btnVerEventosMenu);
        panelMenu.add(btnSacarTicketMenu);

        panelFormulario.setLayout(new BorderLayout());
        Panel camposGrid = new Panel(new GridLayout(6, 2));
        camposGrid.add(new Label("Nombre Cliente:")); camposGrid.add(txtNombre);
        camposGrid.add(new Label("DNI / Email:")); camposGrid.add(txtDni);
        camposGrid.add(new Label("ID Evento:")); camposGrid.add(txtEvento);
        camposGrid.add(new Label("ID Tipo Entrada:")); camposGrid.add(txtTipo);
        camposGrid.add(new Label("Cantidad:")); camposGrid.add(txtCantidad);
        camposGrid.add(btnVolverDeFormulario); 
        camposGrid.add(btnComprar); 
        
        panelFormulario.add(btnVerEventosFormulario, BorderLayout.NORTH);
        panelFormulario.add(camposGrid, BorderLayout.CENTER);

        panelEventos.setLayout(new BorderLayout());
        panelEventos.add(btnVolverDeEventos, BorderLayout.SOUTH);

        areaTexto.setEditable(false);
        ventana.add(areaTexto, BorderLayout.SOUTH);
        mostrarPanelMenu();

        ventana.setSize(400, 450);
        ventana.setResizable(false);
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
    }

    // --- NUEVO MÉTODO: Muestra una ventana emergente (Dialog) de confirmación ---
    public boolean mostrarDialogoConfirmacion(String datosResumen) {
        // Creamos un diálogo modal (el 'true' hace que bloquee la pantalla de atrás)
        final Dialog dialogo = new Dialog(ventana, "Confirmar Compra", true);
        dialogo.setLayout(new BorderLayout());
        
        // Área de texto interna para mostrar los datos que se van a pagar
        TextArea txtResumen = new TextArea(datosResumen, 8, 35, TextArea.SCROLLBARS_NONE);
        txtResumen.setEditable(false);
        dialogo.add(txtResumen, BorderLayout.CENTER);
        
        // Panel inferior para los botones de Confirmar y Cancelar
        Panel panelBotones = new Panel();
        Button btnConfirmar = new Button("Confirmar");
        Button btnCancelar = new Button("Cancelar");
        panelBotones.add(btnConfirmar);
        panelBotones.add(btnCancelar);
        dialogo.add(panelBotones, BorderLayout.SOUTH);
        
        // Variable para guardar la elección del usuario
        final boolean[] resultadoConfirmacion = {false};
        
        // Acción al pulsar Confirmar
        btnConfirmar.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                resultadoConfirmacion[0] = true;
                dialogo.setVisible(false);
                dialogo.dispose(); // Destruye la ventana emergente
            }
        });
        
        // Acción al pulsar Cancelar
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                resultadoConfirmacion[0] = false;
                dialogo.setVisible(false);
                dialogo.dispose();
            }
        });
        
        // Acción por si cierran la ventana desde la "X" del diálogo
        dialogo.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                resultadoConfirmacion[0] = false;
                dialogo.setVisible(false);
                dialogo.dispose();
            }
        });
        
        dialogo.setSize(380, 280);
        dialogo.setLocationRelativeTo(ventana); // Centrado respecto a la app principal
        dialogo.setVisible(true); // Aquí el programa se "detiene" hasta que el diálogo se cierre
        
        return resultadoConfirmacion[0];
    }

    // --- MÉTODOS DE NAVEGACIÓN VISUAL ---
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

    public String getNombre() { return txtNombre.getText(); }
    public String getDni() { return txtDni.getText(); }
    public String getEvento() { return txtEvento.getText(); }
    public String getTipo() { return txtTipo.getText(); }
    public String getCantidad() { return txtCantidad.getText(); }

    public void mostrarMensaje(String mensaje) {
        areaTexto.setText(mensaje);
    }

    public void limpiarFormulario() {
        txtNombre.setText("");
        txtDni.setText("");
        txtEvento.setText("");
        txtTipo.setText("");
        txtCantidad.setText("");
    }
}