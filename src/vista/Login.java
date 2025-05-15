package vista;

import controller.Controlador;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Login extends javax.swing.JFrame {

    public Login() {
        initComponents();
        setLocationRelativeTo(this);
        setIconImage(new ImageIcon("C:\\Users\\MAFOWS21\\Documents\\PC's RESUELTA seccion X\\RETO DE LAS SEMANA 7 (Presentacion)\\S_G_E_V\\src\\main\\java\\imagenes\\login.png").getImage());

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnSALIR = new javax.swing.JButton();
        etiquetaUSUARIO = new javax.swing.JLabel();
        etiquetaCONTRASENA = new javax.swing.JLabel();
        imagenUSUARIO = new javax.swing.JLabel();
        txtUSUARIO = new javax.swing.JTextField();
        txtCONTRASENA = new javax.swing.JPasswordField();
        btnINGRESAR = new javax.swing.JButton();
        imagenFONDO = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnSALIR.setBackground(new java.awt.Color(0, 0, 204));
        btnSALIR.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnSALIR.setForeground(new java.awt.Color(255, 255, 255));
        btnSALIR.setText("Salir");
        btnSALIR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSALIRActionPerformed(evt);
            }
        });
        getContentPane().add(btnSALIR, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 20, -1, -1));

        etiquetaUSUARIO.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        etiquetaUSUARIO.setForeground(new java.awt.Color(255, 255, 255));
        etiquetaUSUARIO.setText("Usuario:");
        getContentPane().add(etiquetaUSUARIO, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 410, -1, -1));

        etiquetaCONTRASENA.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        etiquetaCONTRASENA.setForeground(new java.awt.Color(255, 255, 255));
        etiquetaCONTRASENA.setText("Contraseña:");
        getContentPane().add(etiquetaCONTRASENA, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 470, -1, -1));

        imagenUSUARIO.setIcon(new javax.swing.ImageIcon("C:\\Users\\MAFOWS21\\Documents\\PC's RESUELTA seccion X\\RETO DE LAS SEMANA 7 (Presentacion)\\S_G_E_V\\src\\main\\java\\imagenes\\login.png")); // NOI18N
        getContentPane().add(imagenUSUARIO, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 80, 250, 250));

        txtUSUARIO.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        getContentPane().add(txtUSUARIO, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 410, 210, 30));

        txtCONTRASENA.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        getContentPane().add(txtCONTRASENA, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 470, 210, 30));

        btnINGRESAR.setBackground(new java.awt.Color(0, 0, 204));
        btnINGRESAR.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnINGRESAR.setForeground(new java.awt.Color(255, 255, 255));
        btnINGRESAR.setText("Iniciar Sesión");
        btnINGRESAR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnINGRESARActionPerformed(evt);
            }
        });
        getContentPane().add(btnINGRESAR, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 540, 140, 30));

        imagenFONDO.setIcon(new javax.swing.ImageIcon("C:\\Users\\MAFOWS21\\Documents\\PC's RESUELTA seccion X\\RETO DE LAS SEMANA 7 (Presentacion)\\S_G_E_V\\src\\main\\java\\imagenes\\fondo.png")); // NOI18N
        getContentPane().add(imagenFONDO, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSALIRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSALIRActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_btnSALIRActionPerformed

    //Metodo pertenenciente a LOPEZ
    private boolean EsAlfabetico(String CAD) {
        boolean Sw = true;
        int k = 0;
        // Es más seguro verificar si la cadena no es null antes de obtener su longitud
        if (CAD == null || CAD.isEmpty()) {
            return false; // Una cadena vacía o null no es alfabética en este contexto
        }
        int L = CAD.length();
        while (k < L && Sw) { // Simplificado el while (k <= L-1) a (k < L)
            char E = CAD.charAt(k);
            if (!((('A' <= E) && (E <= 'Z')) || (('a' <= E) && (E <= 'z')))) {
                Sw = false;
            }
            k++;
        }
        return Sw;
    }

    //Metodo pertenciente a LOPEZ
    public String RegistroUsuario(String CAD) {
        boolean Sw = false;
        String REGISTRO = ""; // Inicializar siempre las variables locales
        String LINE; // No es necesario inicializarla aquí

        String rutaArchivoUsuarios = "C:\\Users\\MAFOWS21\\Documents\\PC's RESUELTA seccion X\\RETO DE LAS SEMANA 7 (Presentacion)\\S_G_E_V\\src\\main\\java\\usuarios\\"
                + "Usuarios.txt";

        File FILE = new File(rutaArchivoUsuarios);

        // Verificar si el archivo existe antes de intentar leerlo
        if (!FILE.exists() || !FILE.isFile()) {
            System.err.println("Error: El archivo de usuarios no se encuentra en la ruta: " + rutaArchivoUsuarios);
            JOptionPane.showMessageDialog(this, "Error interno: No se pudo encontrar el archivo de configuración de usuarios.", "Error de Archivo", JOptionPane.ERROR_MESSAGE);
            return ""; // Devolver cadena vacía si el archivo no existe
        }

        // Usar try-with-resources para asegurar que el BufferedReader se cierre
        try (BufferedReader BR = new BufferedReader(new FileReader(FILE))) {
            while (((LINE = BR.readLine()) != null) && !Sw) {
                // Añadir validación para evitar errores si la línea es muy corta
                if (LINE.length() >= 60) {
                    // Comparar la subcadena específica con el usuario ingresado
                    if (LINE.substring(45, 60).trim().equals(CAD)) {
                        Sw = true;
                        REGISTRO = LINE;
                    }
                } else {
                    // Opcional: Informar sobre líneas con formato incorrecto
                    System.err.println("Advertencia: Línea ignorada por formato incorrecto en Usuarios.txt: " + LINE);
                }
            }
        } catch (IOException e) {
            // Mostrar un mensaje de error más específico al usuario y/o log
            System.err.println("Error de E/S al leer el archivo de usuarios: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error al leer los datos de usuario.", "Error de Lectura", JOptionPane.ERROR_MESSAGE);
            REGISTRO = ""; // Asegurarse de devolver vacío en caso de error
        } catch (StringIndexOutOfBoundsException e) {
            // Capturar error si substring intenta acceder fuera de los límites (ya prevenido con length check)
            System.err.println("Error de formato en el archivo de usuarios: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error de formato en los datos de usuario.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            REGISTRO = "";
        }
        return REGISTRO;
    }


    private void btnINGRESARActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnINGRESARActionPerformed

        String usernameIngresado = txtUSUARIO.getText().trim();
        String contrasenaIngresada = new String(txtCONTRASENA.getPassword());
        String contrasenaCorrecta = "1234"; // La contraseña que DEBE coincidir
        String lineaRegistro;

        // --- PASO 1: Validar campos vacíos ---
        if (usernameIngresado.isEmpty()) {
            JOptionPane.showMessageDialog(this, "ERROR: Debe ingresar el nombre de Usuario", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
            txtUSUARIO.requestFocus();
            return;
        }
        if (contrasenaIngresada.isEmpty()) {
            JOptionPane.showMessageDialog(this, "ERROR: Debe ingresar la Contraseña", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
            txtCONTRASENA.requestFocus();
            return;
        }
        // --- FIN PASO 1 ---

        // --- PASO 2: Validar formato y existencia del usuario ---
        // (Incluimos formato como buena práctica, aunque no lo pediste explícitamente aquí)
        // 2a: Validar longitud máxima
        if (usernameIngresado.length() > 11) {
            JOptionPane.showMessageDialog(this, "ERROR: Demasiados Caracteres en Usuario (Máximo 11)", "Error de Entrada", JOptionPane.WARNING_MESSAGE);
            txtUSUARIO.requestFocus();
            return;
        }
        // 2b: Validar caracteres alfabéticos
        if (!EsAlfabetico(usernameIngresado)) {
            JOptionPane.showMessageDialog(this, "ERROR: Caracteres No Válidos en Usuario (Solo letras A-Z, a-z)", "Error de Entrada", JOptionPane.WARNING_MESSAGE);
            txtUSUARIO.requestFocus();
            return;
        }
        // 2c: Buscar usuario en el archivo
        lineaRegistro = RegistroUsuario(usernameIngresado);
        // 2d: Validar si el usuario fue encontrado
        if (lineaRegistro.isEmpty()) {
            JOptionPane.showMessageDialog(this, "ERROR: Usuario No Registrado", "Error de Acceso", JOptionPane.ERROR_MESSAGE);
            txtUSUARIO.requestFocus();
            return; // Detener si el usuario no existe
        }
        // --- FIN PASO 2 ---

        // --- PASO 3: Validar la contraseña ---
        if (!contrasenaCorrecta.equals(contrasenaIngresada)) {
            JOptionPane.showMessageDialog(this, "Error, la contraseña es incorrecta", "Error de Acceso", JOptionPane.ERROR_MESSAGE);
            txtCONTRASENA.requestFocus();
            txtCONTRASENA.selectAll();
            return; // Detener si la contraseña es incorrecta
        }
        // --- FIN PASO 3 ---

        // --- PASO 4: Si usuario existe Y contraseña es correcta, verificar estado ---
        // (Este código solo se ejecuta si los pasos 2 y 3 fueron exitosos)
        if (lineaRegistro.length() <= 104 || lineaRegistro.charAt(104) != '1') {
            // PASO 6: Usuario Inactivo
            JOptionPane.showMessageDialog(this, "ERROR: Usuario Inactivo", "Error de Acceso", JOptionPane.ERROR_MESSAGE);
            txtUSUARIO.requestFocus(); // Opcional: Quizás no quieras mover el foco aquí
            return; // Detener si está inactivo
        }
        // --- FIN PASO 4 ---

        // --- PASO 5: Si todo está bien (usuario existe, contraseña correcta, usuario activo) ---
        JOptionPane.showMessageDialog(this, "Ingresaste al sistema", "Felicidades", JOptionPane.INFORMATION_MESSAGE);
        this.dispose(); // Cerrar la ventana de login

        // Código para iniciar la ventana principal (tu código original)
        Vista vista = new Vista();
        vistaEMPLEADO vemp = new vistaEMPLEADO();
        vistaCLIENTE vclt = new vistaCLIENTE();
        vistaVEHICULO vhcl = new vistaVEHICULO();
        vistaENTRADA veta = new vistaENTRADA();
        vistaSALIDA vsld = new vistaSALIDA();
        mostrarEMPLEADOS mEMP = new mostrarEMPLEADOS();
        mostrarCLIENTES mCLT = new mostrarCLIENTES();
        mostrarVEHICULOS mvhcl = new mostrarVEHICULOS();
        mostrarENTRADAS mETA = new mostrarENTRADAS();
        mostrarSALIDAS msld = new mostrarSALIDAS();
        vistaELIMINAR vdlt = new vistaELIMINAR();
        vistaBUSCAR vbcr = new vistaBUSCAR();
        vistaRESULTADOS vrts = new vistaRESULTADOS();
        vistaACTUALIZAR update = new vistaACTUALIZAR();

        Controlador ctr = new Controlador(vista, vemp, vclt, vhcl, veta, vsld, mEMP,
                mCLT, mvhcl, mETA, msld, vdlt, vbcr, vrts, update);
        vista.txtSALUDO.setText("Bienvenido: " + usernameIngresado.toUpperCase() + "...");
        vista.setVisible(true);
        // --- FIN PASO 5 ---

    }//GEN-LAST:event_btnINGRESARActionPerformed

    /*
    private void btnINGRESARActionPerformed(java.awt.event.ActionEvent evt) {                                            
        
        String usuario = "jorge";
        String contrasenaCorrecta = "1234";
        
        if(usuario.equals(txtUSUARIO.getText())){
            String contrasena = "";
            
            for(int i=0;i<txtCONTRASENA.getPassword().length;i++){
                contrasena += txtCONTRASENA.getPassword()[i];
            }
            
            if(contrasenaCorrecta.equals(contrasena)){
                
                
                JOptionPane.showMessageDialog(null, "Ingresaste al sistema","Felicidades",JOptionPane.INFORMATION_MESSAGE);
                //JOptionPane.showMessageDialog(null,"Ingresaste al Sistema",JOptionPane.INFORMATION_MESSAGE);
                this.dispose();                                                
                
                
                Vista vista = new Vista();
                vistaEMPLEADO vemp = new vistaEMPLEADO();
                vistaCLIENTE vclt = new vistaCLIENTE();
                vistaVEHICULO vhcl = new vistaVEHICULO();
                vistaENTRADA veta = new vistaENTRADA();
                vistaSALIDA vsld = new vistaSALIDA();
                mostrarEMPLEADOS mEMP = new mostrarEMPLEADOS();
                mostrarCLIENTES mCLT = new mostrarCLIENTES();
                mostrarVEHICULOS mvhcl = new mostrarVEHICULOS();
                mostrarENTRADAS mETA = new mostrarENTRADAS();
                mostrarSALIDAS msld = new mostrarSALIDAS();
                vistaELIMINAR vdlt = new vistaELIMINAR();
                vistaBUSCAR vbcr = new vistaBUSCAR();
                vistaRESULTADOS vrts = new vistaRESULTADOS();
                vistaACTUALIZAR update = new vistaACTUALIZAR();

                Controlador ctr = new Controlador(vista, vemp, vclt, vhcl, veta, vsld, mEMP,
                        mCLT, mvhcl, mETA, msld, vdlt, vbcr, vrts, update);
                vista.txtSALUDO.setText("Bienvenido: "+usuario.toUpperCase()+"...");
                
                //vista.setExtendedState(6);
                vista.setVisible(true);
                                                                                                                                                
            }else{
                JOptionPane.showMessageDialog(null, "Error, la contraseña es incorrecta");                 
           }
            
        }
        else{
            JOptionPane.showMessageDialog(null, "Error usuario desconocido");            
        }
        
    }        
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnINGRESAR;
    public javax.swing.JButton btnSALIR;
    private javax.swing.JLabel etiquetaCONTRASENA;
    private javax.swing.JLabel etiquetaUSUARIO;
    private javax.swing.JLabel imagenFONDO;
    private javax.swing.JLabel imagenUSUARIO;
    public javax.swing.JPasswordField txtCONTRASENA;
    public javax.swing.JTextField txtUSUARIO;
    // End of variables declaration//GEN-END:variables
}
