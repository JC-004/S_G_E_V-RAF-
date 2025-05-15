package vista;

import javax.swing.ImageIcon;


public class vistaRESULTADOS extends javax.swing.JFrame {

    
    public vistaRESULTADOS() {
        initComponents();
        setLocationRelativeTo(this);
        setIconImage(new ImageIcon("C:\\Users\\MAFOWS21\\Documents\\PC's RESUELTA seccion X\\RETO DE LAS SEMANA 7 (Presentacion)\\S_G_E_V\\src\\main\\java\\imagenes\\buscar.png").getImage());
    }

    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtESCRIBIR = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaRESULTADOS = new javax.swing.JTable();
        btnREGRESAR = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Mostrando resultados de la busqueda");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtESCRIBIR.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txtESCRIBIR.setText("EDITAME");
        getContentPane().add(txtESCRIBIR, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 10, -1, -1));

        tablaRESULTADOS.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tablaRESULTADOS);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 1280, 130));

        btnREGRESAR.setBackground(new java.awt.Color(51, 153, 0));
        btnREGRESAR.setText("REGRESAR");
        getContentPane().add(btnREGRESAR, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 210, 110, 50));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("Mostrando:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnREGRESAR;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTable tablaRESULTADOS;
    public javax.swing.JLabel txtESCRIBIR;
    // End of variables declaration//GEN-END:variables
}
