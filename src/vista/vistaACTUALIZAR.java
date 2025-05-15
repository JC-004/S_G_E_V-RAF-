
package vista;

import javax.swing.ImageIcon;


public class vistaACTUALIZAR extends javax.swing.JFrame {

    
    public vistaACTUALIZAR() {
        initComponents();
        setLocationRelativeTo(this);
        
        setIconImage(new ImageIcon("C:\\Users\\MAFOWS21\\Documents\\PC's RESUELTA seccion X\\RETO DE LAS SEMANA 7 (Presentacion)\\S_G_E_V\\src\\main\\java\\imagenes\\actualizar.png").getImage());
        
        
        
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        cbxID = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cbxTIPO = new javax.swing.JComboBox<>();
        btnACTUALIZAR = new javax.swing.JButton();
        btnREGRESAR = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Actualizar en el registro");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 102, 204));
        jLabel1.setText("Actualizar en el registro");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 10, 190, -1));

        getContentPane().add(cbxID, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 120, 120, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("ID:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, -1, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Tipo:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 60, -1, -1));

        cbxTIPO.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Empleado", "Cliente", "Vehiculo", "Entrada", "Salida" }));
        cbxTIPO.setSelectedIndex(-1);
        getContentPane().add(cbxTIPO, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 60, 120, -1));

        btnACTUALIZAR.setBackground(new java.awt.Color(153, 0, 102));
        btnACTUALIZAR.setText("ACTUALIZAR");
        getContentPane().add(btnACTUALIZAR, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 210, 110, 50));

        btnREGRESAR.setBackground(new java.awt.Color(51, 153, 0));
        btnREGRESAR.setText("REGRESAR");
        getContentPane().add(btnREGRESAR, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 210, 110, 50));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnACTUALIZAR;
    public javax.swing.JButton btnREGRESAR;
    public javax.swing.JComboBox<String> cbxID;
    public javax.swing.JComboBox<String> cbxTIPO;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    // End of variables declaration//GEN-END:variables
}
