package view;

import dao.ClienteDAO;
import model.Cliente;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.util.List;
import java.util.regex.Pattern;

public class ClienteForm extends JFrame {
    private JPanel panelMain;
    private JTextField txtNome;
    private JFormattedTextField txtCPF;
    private JFormattedTextField txtTelefone;
    private JTextField txtEmail;
    private JButton btnSalvar;
    private JButton btnBuscar;
    private JButton btnAlterar;
    private JButton btnDeletar;
    private JButton btnListar;
    private JTextArea txtResultados;
    private JScrollPane scrollPane;

    public ClienteForm() {
        initComponents();
        setupLayout();
        setupMasks();
        setupActions();
        setVisible(true);
    }

    private void initComponents() {
        panelMain = new JPanel();
        txtNome = new JTextField(20);
        txtEmail = new JTextField(20);
        txtCPF = new JFormattedTextField();
        txtTelefone = new JFormattedTextField();

        btnSalvar = new JButton("Salvar");
        btnBuscar = new JButton("Buscar");
        btnAlterar = new JButton("Alterar");
        btnDeletar = new JButton("Deletar");
        btnListar = new JButton("Listar Todos");

        txtResultados = new JTextArea(10, 30);
        txtResultados.setEditable(false);
        scrollPane = new JScrollPane(txtResultados);
    }

    private void setupLayout() {
        panelMain.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Linha 0 - Nome
        gbc.gridy = 0;
        gbc.gridx = 0;
        panelMain.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        panelMain.add(txtNome, gbc);

        // Linha 1 - CPF
        gbc.gridy = 1;
        gbc.gridx = 0;
        panelMain.add(new JLabel("CPF:"), gbc);
        gbc.gridx = 1;
        txtCPF.setColumns(20);
        panelMain.add(txtCPF, gbc);

        // Linha 2 - Telefone
        gbc.gridy = 2;
        gbc.gridx = 0;
        panelMain.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 1;
        txtTelefone.setColumns(20);
        panelMain.add(txtTelefone, gbc);

        // Linha 3 - Email
        gbc.gridy = 3;
        gbc.gridx = 0;
        panelMain.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panelMain.add(txtEmail, gbc);

        // Linha 4 - Botões linha 1
        gbc.gridy = 4;
        gbc.gridx = 0;
        panelMain.add(btnSalvar, gbc);
        gbc.gridx = 1;
        panelMain.add(btnBuscar, gbc);

        // Linha 5 - Botões linha 2
        gbc.gridy = 5;
        gbc.gridx = 0;
        panelMain.add(btnAlterar, gbc);
        gbc.gridx = 1;
        panelMain.add(btnDeletar, gbc);

        // Linha 6 - Botão listar
        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panelMain.add(btnListar, gbc);

        // Linha 7 - Resultados
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.BOTH;
        panelMain.add(scrollPane, gbc);

        setContentPane(panelMain);
        setTitle("Cadastro de Cliente");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void setupMasks() {
        try {
            MaskFormatter cpfFormatter = new MaskFormatter("###.###.###-##");
            cpfFormatter.setPlaceholderCharacter('_');
            cpfFormatter.setValueContainsLiteralCharacters(false);
            txtCPF.setFormatterFactory(new DefaultFormatterFactory(cpfFormatter));

            MaskFormatter telefoneFormatter = new MaskFormatter("(##) #####-####");
            telefoneFormatter.setPlaceholderCharacter('_');
            telefoneFormatter.setValueContainsLiteralCharacters(false);
            txtTelefone.setFormatterFactory(new DefaultFormatterFactory(telefoneFormatter));
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Erro ao configurar máscaras: " + e.getMessage());
        }
    }

    private void setupActions() {
        ClienteDAO dao = new ClienteDAO();

        btnSalvar.addActionListener(e -> {
            if (!validarCampos()) return;

            int confirm = JOptionPane.showConfirmDialog(this, "Confirmar cadastro deste cliente?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Cliente c = criarClienteFromForm();
                    dao.inserir(c);
                    JOptionPane.showMessageDialog(this, "Cliente salvo com sucesso!");
                    limparCampos();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao salvar cliente: " + ex.getMessage());
                }
            }
        });

        btnBuscar.addActionListener(e -> {
            if (!validarCPF()) return;

            try {
                Cliente c = dao.buscarPorCPF(txtCPF.getText().replaceAll("[^0-9]", ""));
                if (c != null) {
                    preencherFormComCliente(c);
                } else {
                    JOptionPane.showMessageDialog(this, "Cliente não encontrado!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao buscar cliente: " + ex.getMessage());
            }
        });

        btnAlterar.addActionListener(e -> {
            if (!validarCampos()) return;

            int confirm = JOptionPane.showConfirmDialog(this, "Confirmar alteração deste cliente?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Cliente c = criarClienteFromForm();
                    dao.atualizar(c);
                    JOptionPane.showMessageDialog(this, "Cliente atualizado com sucesso!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao atualizar cliente: " + ex.getMessage());
                }
            }
        });

        btnDeletar.addActionListener(e -> {
            if (!validarCPF()) return;

            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir este cliente?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    String cpf = txtCPF.getText().replaceAll("[^0-9]", "");
                    dao.deletarPorCPF(cpf);
                    JOptionPane.showMessageDialog(this, "Cliente deletado com sucesso!");
                    limparCampos();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao deletar cliente: " + ex.getMessage());
                }
            }
        });

        btnListar.addActionListener(e -> {
            try {
                List<Cliente> clientes = dao.listarTodos();
                if (clientes.isEmpty()) {
                    txtResultados.setText("Nenhum cliente cadastrado.");
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (Cliente c : clientes) {
                        sb.append(String.format("ID: %d | Nome: %s | CPF: %s | Telefone: %s | Email: %s%n",
                                c.getId(), c.getNome(), formatarCPF(c.getCpf()), formatarTelefone(c.getTelefone()), c.getEmail()));
                    }
                    txtResultados.setText(sb.toString());
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao listar clientes: " + ex.getMessage());
            }
        });
    }

    private boolean validarCampos() {
        if (txtNome.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O campo Nome é obrigatório!");
            txtNome.requestFocus();
            return false;
        }

        if (!validarCPF()) return false;

        String telefone = txtTelefone.getText();
        if (!telefone.trim().isEmpty() && telefone.contains("_")) {
            JOptionPane.showMessageDialog(this, "Telefone incompleto!");
            txtTelefone.requestFocus();
            return false;
        }

        String email = txtEmail.getText().trim();
        if (!email.isEmpty() && !validarEmail(email)) {
            JOptionPane.showMessageDialog(this, "Email inválido!");
            txtEmail.requestFocus();
            return false;
        }

        return true;
    }

    private boolean validarCPF() {
        String cpf = txtCPF.getText();
        if (cpf.trim().isEmpty() || cpf.contains("_")) {
            JOptionPane.showMessageDialog(this, "CPF inválido ou incompleto!");
            txtCPF.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validarEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.compile(regex).matcher(email).matches();
    }

    private Cliente criarClienteFromForm() {
        Cliente c = new Cliente();
        c.setNome(txtNome.getText().trim());
        c.setCpf(txtCPF.getText().replaceAll("[^0-9]", ""));
        c.setTelefone(txtTelefone.getText().replaceAll("[^0-9]", ""));
        c.setEmail(txtEmail.getText().trim());
        return c;
    }

    private void preencherFormComCliente(Cliente c) {
        txtNome.setText(c.getNome());
        txtEmail.setText(c.getEmail());

        try {
            MaskFormatter cpfFormatter = new MaskFormatter("###.###.###-##");
            cpfFormatter.setValueContainsLiteralCharacters(false);
            txtCPF.setFormatterFactory(new DefaultFormatterFactory(cpfFormatter));
            txtCPF.setValue(c.getCpf());

            if (c.getTelefone() != null && !c.getTelefone().isEmpty()) {
                MaskFormatter telefoneFormatter = new MaskFormatter("(##) #####-####");
                telefoneFormatter.setValueContainsLiteralCharacters(false);
                txtTelefone.setFormatterFactory(new DefaultFormatterFactory(telefoneFormatter));
                txtTelefone.setValue(c.getTelefone());
            } else {
                txtTelefone.setValue(null);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        txtEmail.setText("");
        txtCPF.setValue(null);
        txtTelefone.setValue(null);
        txtResultados.setText("");
    }

    private String formatarCPF(String cpf) {
        if (cpf == null || cpf.length() != 11) return cpf;
        return cpf.replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    private String formatarTelefone(String telefone) {
        if (telefone == null || telefone.length() != 11) return telefone;
        return telefone.replaceFirst("(\\d{2})(\\d{5})(\\d{4})", "($1) $2-$3");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new ClienteForm().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro ao iniciar a aplicação: " + e.getMessage());
            }
        });
    }
}
