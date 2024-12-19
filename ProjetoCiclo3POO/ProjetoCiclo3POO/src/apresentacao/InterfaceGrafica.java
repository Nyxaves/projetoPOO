package apresentacao;

import negocio.*;
import acesso_aos_dados.*;

import javax.swing.*;
import java.awt.*;

public class InterfaceGrafica extends JFrame {

    private Banco banco;

    public InterfaceGrafica() {
        ConexaoBanco conexao = new ConexaoBanco();
        this.banco = new Banco(conexao);
        setTitle("Sistema Bancário");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel painelSuperior = new JPanel();
        painelSuperior.add(new JLabel("Bem-vindo ao Sistema Bancário"));

        JPanel painelCentral = criarPainelCentral();

        add(painelSuperior, BorderLayout.NORTH);
        add(painelCentral, BorderLayout.CENTER);
    }

    private JPanel criarPainelCentral() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        JPanel painelEntrada = new JPanel(new GridLayout(4, 2, 10, 10));

        JLabel lblNumero = new JLabel("Número da Conta:");
        JTextField txtNumero = new JTextField();

        JLabel lblSaldo = new JLabel("Saldo Inicial:");
        JTextField txtSaldo = new JTextField();

        JLabel lblLimite = new JLabel("Limite (opcional):");
        JTextField txtLimite = new JTextField();

        JButton btnCriarNormal = new JButton("Criar Conta Normal");
        JButton btnCriarEspecial = new JButton("Criar Conta Especial");
        JButton btnListarContas = new JButton("Listar Contas");

        JTextArea txtSaida = new JTextArea(10, 30);
        txtSaida.setEditable(false);

        painelEntrada.add(lblNumero);
        painelEntrada.add(txtNumero);
        painelEntrada.add(lblSaldo);
        painelEntrada.add(txtSaldo);
        painelEntrada.add(lblLimite);
        painelEntrada.add(txtLimite);
        painelEntrada.add(btnCriarNormal);
        painelEntrada.add(btnCriarEspecial);

        painel.add(painelEntrada, BorderLayout.NORTH);
        painel.add(btnListarContas, BorderLayout.CENTER);
        painel.add(new JScrollPane(txtSaida), BorderLayout.SOUTH);

        btnCriarNormal.addActionListener(e -> criarContaNormal(txtNumero, txtSaldo, txtSaida));
        btnCriarEspecial.addActionListener(e -> criarContaEspecial(txtNumero, txtSaldo, txtLimite, txtSaida));
        btnListarContas.addActionListener(e -> listarContas(txtSaida));

        return painel;
    }

    private void criarContaNormal(JTextField txtNumero, JTextField txtSaldo, JTextArea txtSaida) {
        try {
            String numero = txtNumero.getText();
            double saldo = Double.parseDouble(txtSaldo.getText());
            Conta conta = new ContaNormal(numero, saldo);
            banco.criarConta(conta);
            txtSaida.append("Conta Normal criada: " + numero + "\n");
        } catch (NumberFormatException ex) {
            txtSaida.append("Erro: Verifique os valores de saldo e número.\n");
        }
    }

    private void criarContaEspecial(JTextField txtNumero, JTextField txtSaldo, JTextField txtLimite, JTextArea txtSaida) {
        try {
            String numero = txtNumero.getText();
            double saldo = Double.parseDouble(txtSaldo.getText());
            double limite = Double.parseDouble(txtLimite.getText());
            Conta conta = new ContaDebEspecial(numero, saldo, limite);
            banco.criarConta(conta);
            txtSaida.append("Conta Especial criada: " + numero + "\n");
        } catch (NumberFormatException ex) {
            txtSaida.append("Erro: Verifique os valores de saldo, limite e número.\n");
        }
    }

    private void listarContas(JTextArea txtSaida) {
        txtSaida.append("Lista de Contas:\n");
        banco.listarContas().forEach(conta ->
            txtSaida.append("Conta: " + conta.getNumero() + ", Saldo: " + conta.getSaldo() + "\n")
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InterfaceGrafica app = new InterfaceGrafica();
            app.setVisible(true);
        });
    }
}
