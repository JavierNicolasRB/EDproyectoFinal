package finalproyecto;

import java.awt.*;

/**
 * Clase Vista que contiene todos los componentes gráficos (AWT) de la aplicación.
 * Define la estructura de las diferentes pantallas (Menú, Eventos, Formulario).
 */
public class Vista {
	Frame ventana = new Frame("Taquilla Plaza de Toros");

	// --- NUEVO: Botones exclusivos para la primera página (Menú) y navegación ---
	Button btnVerEventosMenu = new Button("Ver Eventos");
	Button btnSacarTicketMenu = new Button("Sacar Ticket");
	Button btnVolver = new Button("Volver al Menú"); 

	// --- TUS COMPONENTES ORIGINALES ---
	Button btnVerEventos = new Button("Ver Eventos Disponibles");
	Button btnComprar = new Button("Confirmar y Comprar Ticket");

	Label lblNombre = new Label("Nombre Cliente:");
	TextField txtNombre = new TextField(20);

	Label lblDni = new Label("DNI / Email:");
	TextField txtDni = new TextField(20);

	Label lblEvento = new Label("ID Evento (1 - 6):");
	TextField txtEvento = new TextField(5);

	Label lblTipo = new Label("Tipo Entrada (1=Sol, 2=Sombra):");
	TextField txtTipo = new TextField(5);

	Label lblCantidad = new Label("Cantidad:");
	TextField txtCantidad = new TextField(5);

	TextArea areaTexto = new TextArea(10, 40);

	// Paneles independientes para simular el cambio de pantallas
	Panel panelMenu = new Panel();
	Panel panelFormulario = new Panel();
	Panel panelEventos = new Panel();

	/**
	 * Constructor de la Vista.
	 * Inicializa y configura la posición de los botones, paneles, y campos de texto
	 * en la ventana principal.
	 */
	public Vista() {
		ventana.setLayout(new BorderLayout());

		// 1. Configuración de la Primera Página (Menú)
		panelMenu.setLayout(new GridLayout(2, 1, 10, 10));
		panelMenu.add(btnVerEventosMenu);
		panelMenu.add(btnSacarTicketMenu);

		// 2. Configuración de Sacar Tickets (Tu diseño con el botón arriba)
		panelFormulario.setLayout(new BorderLayout());
		
		Panel camposGrid = new Panel(new GridLayout(6, 2));
		camposGrid.add(lblNombre);
		camposGrid.add(txtNombre);
		camposGrid.add(lblDni);
		camposGrid.add(txtDni);
		camposGrid.add(lblEvento);
		camposGrid.add(txtEvento);
		camposGrid.add(lblTipo);
		camposGrid.add(txtTipo);
		camposGrid.add(lblCantidad);
		camposGrid.add(txtCantidad);
		camposGrid.add(btnVolver); // Botón para regresar al menú principal
		camposGrid.add(btnComprar); // Tu botón original de registrar la compra
		
		panelFormulario.add(btnVerEventos, BorderLayout.NORTH); // Tu botón original de consultar arriba
		panelFormulario.add(camposGrid, BorderLayout.CENTER);

		// 3. Configuración de Ver Eventos (Solo muestra la lista limpia)
		panelEventos.setLayout(new BorderLayout());
		panelEventos.add(btnVolver, BorderLayout.SOUTH);

		// Estado por defecto: Añadimos el menú inicial en el centro
		ventana.add(panelMenu, BorderLayout.CENTER);

		// Tu área de texto original fija abajo del todo
		areaTexto.setEditable(false);
		ventana.add(areaTexto, BorderLayout.SOUTH);

		ventana.setSize(400, 450);
		ventana.setResizable(false);
		ventana.setLocationRelativeTo(null);
		ventana.setVisible(true);
	}
}