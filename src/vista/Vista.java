package vista;

import imagenes.JPanelImage;
import java.awt.Color;
import javax.swing.ImageIcon;


public class Vista extends javax.swing.JFrame {

    
    public Vista() {
        initComponents();
        setLocationRelativeTo(this);
        
        //cambiar el icono por defecto de NETBEANS        
        setIconImage(new ImageIcon("C:\\Users\\MAFOWS21\\Documents\\PC's RESUELTA seccion X\\RETO DE LAS SEMANA 7 (Presentacion)\\S_G_E_V\\src\\main\\java\\imagenes\\estacionamiento.png").getImage());
        
        
        //jpIMAGEN 
        String absolutePath = "C:\\Users\\MAFOWS21\\Documents\\PC's RESUELTA seccion X\\RETO DE LAS SEMANA 7 (Presentacion)\\S_G_E_V\\src\\main\\java\\imagenes\\carro.png";
        JPanelImage img = new JPanelImage(jpIMAGEN, absolutePath);
        jpIMAGEN.add(img);
        jpIMAGEN.repaint();
        jpIMAGEN.setOpaque(false);
        jpIMAGEN.setBorder(null);
        jpIMAGEN.setBackground(new Color(0, 0, 0, 0));
    }
    
    
    


    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtSALUDO = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jpIMAGEN = new javax.swing.JPanel();
        brrMENU = new javax.swing.JMenuBar();
        menuRegistrar = new javax.swing.JMenu();
        Empleado = new javax.swing.JMenuItem();
        Cliente = new javax.swing.JMenuItem();
        Vehiculo = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        Entrada = new javax.swing.JMenuItem();
        Salida = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        Salir = new javax.swing.JMenuItem();
        menuMostrar = new javax.swing.JMenu();
        mostrarEMPLEADOS = new javax.swing.JMenuItem();
        mostrarCLIENTES = new javax.swing.JMenuItem();
        mostrarVEHICULOS = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        mostrarENTRADAS = new javax.swing.JMenuItem();
        mostrarSALIDAS = new javax.swing.JMenuItem();
        menuELIMINAR = new javax.swing.JMenu();
        eliminarMN = new javax.swing.JMenuItem();
        menuACTUALIZAR = new javax.swing.JMenu();
        actualizarMN = new javax.swing.JMenuItem();
        menuBUSCAR = new javax.swing.JMenu();
        buscarMN = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistema de Gestión de Estacionamiento de Vehículos");
        setMinimumSize(new java.awt.Dimension(775, 503));
        setPreferredSize(new java.awt.Dimension(775, 503));
        setResizable(false);

        txtSALUDO.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        txtSALUDO.setForeground(new java.awt.Color(0, 0, 0));

        jLabel2.setFont(new java.awt.Font("Arial", 1, 22)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 153, 255));
        jLabel2.setText("<html>SISTEMA DE GESTIÓN DE ESTACIONAMIENTO DE VEHICULOS</html>");

        javax.swing.GroupLayout jpIMAGENLayout = new javax.swing.GroupLayout(jpIMAGEN);
        jpIMAGEN.setLayout(jpIMAGENLayout);
        jpIMAGENLayout.setHorizontalGroup(
            jpIMAGENLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 602, Short.MAX_VALUE)
        );
        jpIMAGENLayout.setVerticalGroup(
            jpIMAGENLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 209, Short.MAX_VALUE)
        );

        brrMENU.setBackground(new java.awt.Color(255, 204, 51));
        brrMENU.setForeground(new java.awt.Color(255, 0, 102));

        menuRegistrar.setIcon(new javax.swing.ImageIcon("C:\\Users\\MAFOWS21\\Documents\\PC's RESUELTA seccion X\\RETO DE LAS SEMANA 7 (Presentacion)\\S_G_E_V\\src\\main\\java\\imagenes\\registrar.png"));
        menuRegistrar.setText("Registrar");
        menuRegistrar.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        Empleado.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Empleado.setIcon(new javax.swing.ImageIcon("C:\\Users\\MAFOWS21\\Documents\\PC's RESUELTA seccion X\\RETO DE LAS SEMANA 7 (Presentacion)\\S_G_E_V\\src\\main\\java\\imagenes\\empleado.png"));
        Empleado.setText("Empleado");
        Empleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EmpleadoActionPerformed(evt);
            }
        });
        menuRegistrar.add(Empleado);

        Cliente.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Cliente.setIcon(new javax.swing.ImageIcon("C:\\Users\\MAFOWS21\\Documents\\PC's RESUELTA seccion X\\RETO DE LAS SEMANA 7 (Presentacion)\\S_G_E_V\\src\\main\\java\\imagenes\\cliente.png"));
        Cliente.setText("Cliente");
        Cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClienteActionPerformed(evt);
            }
        });
        menuRegistrar.add(Cliente);

        Vehiculo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Vehiculo.setIcon(new javax.swing.ImageIcon("C:\\Users\\MAFOWS21\\Documents\\PC's RESUELTA seccion X\\RETO DE LAS SEMANA 7 (Presentacion)\\S_G_E_V\\src\\main\\java\\imagenes\\vehiculo.png"));
        Vehiculo.setText("Vehiculo");
        Vehiculo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VehiculoActionPerformed(evt);
            }
        });
        menuRegistrar.add(Vehiculo);
        menuRegistrar.add(jSeparator1);

        Entrada.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Entrada.setIcon(new javax.swing.ImageIcon("C:\\Users\\MAFOWS21\\Documents\\PC's RESUELTA seccion X\\RETO DE LAS SEMANA 7 (Presentacion)\\S_G_E_V\\src\\main\\java\\imagenes\\entrada.png"));
        Entrada.setText("Entrada");
        menuRegistrar.add(Entrada);

        Salida.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Salida.setIcon(new javax.swing.ImageIcon("C:\\Users\\MAFOWS21\\Documents\\PC's RESUELTA seccion X\\RETO DE LAS SEMANA 7 (Presentacion)\\S_G_E_V\\src\\main\\java\\imagenes\\salida.png"));
        Salida.setText("Salida");
        menuRegistrar.add(Salida);
        menuRegistrar.add(jSeparator3);

        Salir.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Salir.setIcon(new javax.swing.ImageIcon("C:\\Users\\MAFOWS21\\Documents\\PC's RESUELTA seccion X\\RETO DE LAS SEMANA 7 (Presentacion)\\S_G_E_V\\src\\main\\java\\imagenes\\salir.png"));
        Salir.setText("Salir");
        Salir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SalirActionPerformed(evt);
            }
        });
        menuRegistrar.add(Salir);

        brrMENU.add(menuRegistrar);

        menuMostrar.setIcon(new javax.swing.ImageIcon("C:\\Users\\MAFOWS21\\Documents\\PC's RESUELTA seccion X\\RETO DE LAS SEMANA 7 (Presentacion)\\S_G_E_V\\src\\main\\java\\imagenes\\tabla.png"));
        menuMostrar.setText("Mostrar");
        menuMostrar.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        mostrarEMPLEADOS.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        mostrarEMPLEADOS.setIcon(new javax.swing.ImageIcon("C:\\Users\\MAFOWS21\\Documents\\PC's RESUELTA seccion X\\RETO DE LAS SEMANA 7 (Presentacion)\\S_G_E_V\\src\\main\\java\\imagenes\\empleado.png"));
        mostrarEMPLEADOS.setText("Empleados");
        mostrarEMPLEADOS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mostrarEMPLEADOSActionPerformed(evt);
            }
        });
        menuMostrar.add(mostrarEMPLEADOS);

        mostrarCLIENTES.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        mostrarCLIENTES.setIcon(new javax.swing.ImageIcon("C:\\Users\\MAFOWS21\\Documents\\PC's RESUELTA seccion X\\RETO DE LAS SEMANA 7 (Presentacion)\\S_G_E_V\\src\\main\\java\\imagenes\\cliente.png"));
        mostrarCLIENTES.setText("Clientes");
        mostrarCLIENTES.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mostrarCLIENTESActionPerformed(evt);
            }
        });
        menuMostrar.add(mostrarCLIENTES);

        mostrarVEHICULOS.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        mostrarVEHICULOS.setIcon(new javax.swing.ImageIcon("C:\\Users\\MAFOWS21\\Documents\\PC's RESUELTA seccion X\\RETO DE LAS SEMANA 7 (Presentacion)\\S_G_E_V\\src\\main\\java\\imagenes\\vehiculo.png"));
        mostrarVEHICULOS.setText("Vehiculos");
        menuMostrar.add(mostrarVEHICULOS);
        menuMostrar.add(jSeparator2);

        mostrarENTRADAS.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        mostrarENTRADAS.setIcon(new javax.swing.ImageIcon("C:\\Users\\MAFOWS21\\Documents\\PC's RESUELTA seccion X\\RETO DE LAS SEMANA 7 (Presentacion)\\S_G_E_V\\src\\main\\java\\imagenes\\entrada.png"));
        mostrarENTRADAS.setText("Entradas");
        menuMostrar.add(mostrarENTRADAS);

        mostrarSALIDAS.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        mostrarSALIDAS.setIcon(new javax.swing.ImageIcon("C:\\Users\\MAFOWS21\\Documents\\PC's RESUELTA seccion X\\RETO DE LAS SEMANA 7 (Presentacion)\\S_G_E_V\\src\\main\\java\\imagenes\\salida.png"));
        mostrarSALIDAS.setText("Salidas");
        menuMostrar.add(mostrarSALIDAS);

        brrMENU.add(menuMostrar);

        menuELIMINAR.setIcon(new javax.swing.ImageIcon("C:\\Users\\MAFOWS21\\Documents\\PC's RESUELTA seccion X\\RETO DE LAS SEMANA 7 (Presentacion)\\S_G_E_V\\src\\main\\java\\imagenes\\eliminar.png"));
        menuELIMINAR.setText("Eliminar");
        menuELIMINAR.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        eliminarMN.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        eliminarMN.setText("Eliminar...");
        menuELIMINAR.add(eliminarMN);

        brrMENU.add(menuELIMINAR);

        menuACTUALIZAR.setIcon(new javax.swing.ImageIcon("C:\\Users\\MAFOWS21\\Documents\\PC's RESUELTA seccion X\\RETO DE LAS SEMANA 7 (Presentacion)\\S_G_E_V\\src\\main\\java\\imagenes\\actualizar.png"));
        menuACTUALIZAR.setText("Actualizar");
        menuACTUALIZAR.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        actualizarMN.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        actualizarMN.setText("Actualizar...");
        menuACTUALIZAR.add(actualizarMN);

        brrMENU.add(menuACTUALIZAR);

        menuBUSCAR.setIcon(new javax.swing.ImageIcon("C:\\Users\\MAFOWS21\\Documents\\PC's RESUELTA seccion X\\RETO DE LAS SEMANA 7 (Presentacion)\\S_G_E_V\\src\\main\\java\\imagenes\\buscar.png"));
        menuBUSCAR.setText("Buscar");
        menuBUSCAR.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        buscarMN.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        buscarMN.setText("Buscar...");
        menuBUSCAR.add(buscarMN);

        brrMENU.add(menuBUSCAR);

        setJMenuBar(brrMENU);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(44, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtSALUDO, javax.swing.GroupLayout.PREFERRED_SIZE, 494, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(51, 51, 51)
                            .addComponent(jpIMAGEN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(76, 76, 76)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 686, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jpIMAGEN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtSALUDO, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void SalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SalirActionPerformed
        // TODO add your handling code here:
        
        
    }//GEN-LAST:event_SalirActionPerformed

    private void EmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EmpleadoActionPerformed
        
    }//GEN-LAST:event_EmpleadoActionPerformed

    private void ClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClienteActionPerformed
        
        
    }//GEN-LAST:event_ClienteActionPerformed

    private void mostrarEMPLEADOSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mostrarEMPLEADOSActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mostrarEMPLEADOSActionPerformed

    private void mostrarCLIENTESActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mostrarCLIENTESActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mostrarCLIENTESActionPerformed

    private void VehiculoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VehiculoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_VehiculoActionPerformed

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JMenuItem Cliente;
    public javax.swing.JMenuItem Empleado;
    public javax.swing.JMenuItem Entrada;
    public javax.swing.JMenuItem Salida;
    public javax.swing.JMenuItem Salir;
    public javax.swing.JMenuItem Vehiculo;
    public javax.swing.JMenuItem actualizarMN;
    public javax.swing.JMenuBar brrMENU;
    public javax.swing.JMenuItem buscarMN;
    public javax.swing.JMenuItem eliminarMN;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPanel jpIMAGEN;
    public javax.swing.JMenu menuACTUALIZAR;
    public javax.swing.JMenu menuBUSCAR;
    public javax.swing.JMenu menuELIMINAR;
    public javax.swing.JMenu menuMostrar;
    public javax.swing.JMenu menuRegistrar;
    public javax.swing.JMenuItem mostrarCLIENTES;
    public javax.swing.JMenuItem mostrarEMPLEADOS;
    public javax.swing.JMenuItem mostrarENTRADAS;
    public javax.swing.JMenuItem mostrarSALIDAS;
    public javax.swing.JMenuItem mostrarVEHICULOS;
    public javax.swing.JLabel txtSALUDO;
    // End of variables declaration//GEN-END:variables
}
