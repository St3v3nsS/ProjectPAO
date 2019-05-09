import exceptions.NoNameException;
import exceptions.OccupiedSeatException;
import exceptions.TooManySeatsException;
import model.*;
import org.jetbrains.annotations.NotNull;
import service.ServiceAPI;
import service.ServiceApiImplFiles;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class App {


    public static final String SPECTACLES = "Spectacles";
    public static final String ACTIONS = "Actions";
    public static final String CLIENT = "Client";
    public static final String NAME = "Please enter your name...";
    public static final String ADD_SPECTACLE = "Add spectacle";
    public static final String ENTER_PASSWORD = "Enter Password";
    public static final String OK = "OK";
    public static final String MOVIES = "Movies";
    public static final String THEATRES = "Theatres";

    private ServiceAPI serviceAPI;
    private String lastPanel;
    private String source = ACTIONS;
    private List<Integer> selectedSeats;

    private JFrame mainFrame;
    private JPanel mainPanel;
    private JLabel title;
    private JPanel buttons;
    private JButton addSpectacleButton;
    private JButton addClientButton;
    private JButton selectSpectacleButton;
    private JTextField clientNameTextField;
    private JLabel areYouVipLabel;
    private JCheckBox vipCheckBox;
    private JCheckBox notVipCheckBox;
    private ButtonGroup buttonGroup;
    private JButton showToPayButton;
    private JButton submitClientButton;
    private JLabel adminDetailsLabel;
    private JTextField usernameTextField;
    private JPasswordField adminPasswordField;
    private JButton submitButton;
    private JButton submitSeatsButton;
    private JButton addMovieButton;
    private JButton addTheatreButton;

    public App(){

        mainPanel.setLayout(new CardLayout());
        serviceAPI = new ServiceApiImplFiles();
        mainFrame = new JFrame();
        setMainFrame();
        setTitle();

        setSelectSpectacleButtonDetails();
        setAddClientButtonDetails();
        setAddSpectacleButtonDetails();

        createButtonPanel();
        createSpectaclePanel();
        createClientPanel();
        createAddSpectaclePanel();
        createNavPanel();

        mainFrame.add(mainPanel, BorderLayout.CENTER);
        mainFrame.setVisible(true);

    }

    private void createTheatrePanel() {

        int numOfTheatres = serviceAPI.getNextTheatres().size();
        List<String> specsNum = serviceAPI.getNextTheatres()
                .stream()
                .map(Theatre::getName)
                .collect(Collectors.toList());

        System.out.println(specsNum);

        JPanel theatrePanel = new JPanel();
        theatrePanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(10,10,10,10);

        checkForNoMovies(specsNum, theatrePanel);

        for(int i = 0; i < numOfTheatres; i++){

            JButton button = new JButton();
            constraints.gridy = i + 1;
            int finalI = i;

            button.setMargin(new Insets(10,10,10,10));
            button.setPreferredSize(new Dimension(300, 30));
            button.addActionListener(ev ->{
                serviceAPI.addSpectacle(serviceAPI.getNextTheatres().get(finalI));
                JOptionPane.showMessageDialog(mainFrame, "Successfully added " + specsNum.get(finalI));
                CardLayout cl = (CardLayout) mainPanel.getLayout();
                cl.show(mainPanel, ACTIONS);
            });
            theatrePanel.add(button, constraints);
            button.setText(specsNum.get(i));
        }

        theatrePanel.setName(THEATRES);
        theatrePanel.setVisible(true);
        mainPanel.add(theatrePanel, THEATRES);
    }

    private void createMoviePanel() {
        int numOfMovies = serviceAPI.getNextMovies().size();
        List<String> specsNum = serviceAPI.getNextMovies()
                .stream()
                .map(Movie::getName)
                .collect(Collectors.toList());

        System.out.println(specsNum);

        JPanel moviePanel = new JPanel();
        moviePanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(10,10,10,10);

        moviePanel = checkForNoMovies(specsNum, moviePanel);

        for(int i = 0; i < numOfMovies; i++){

            JButton button = new JButton();

            int finalI = i;
            constraints.gridy = i+1;

            button.setPreferredSize(new Dimension(300, 30));
            button.setMargin(new Insets(10,10,10,10));
            button.addActionListener(ev->{
                serviceAPI.addSpectacle(serviceAPI.getNextMovies().get(finalI));
                JOptionPane.showMessageDialog(mainFrame, "Successfully added " + specsNum.get(finalI));
                CardLayout cl = (CardLayout) mainPanel.getLayout();
                cl.show(mainPanel, ACTIONS);
            });
            moviePanel.add(button, constraints);
            button.setText(specsNum.get(i));
        }

        moviePanel.setName(MOVIES);
        moviePanel.setVisible(true);
        mainPanel.add(moviePanel, MOVIES);

    }

    private JPanel checkForNoMovies(List<String> specsNum, JPanel moviePanel) {
        if(specsNum.size() == 0){
            JLabel label = new JLabel("Nothing new to add!");
            moviePanel.setLayout(new GridBagLayout());
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.anchor = GridBagConstraints.CENTER;
            label.setFont(label.getFont().deriveFont(20.0f));

            moviePanel.add(label, constraints);
        }

        return moviePanel;
    }

    private void createAddSpectaclePanel() {

        adminDetailsLabel = new JLabel("Please Log in into admin user");
        usernameTextField = new JTextField("Admin");
        adminPasswordField = new JPasswordField(ENTER_PASSWORD);
        submitButton = new JButton("Enter admin");

        usernameTextField.setPreferredSize(new Dimension(200, 30));
        usernameTextField.setEnabled(false);

        setAdminPasswordFieldDetails();

        JPanel addSpec = new JPanel();
        addSpec.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(30,30,30,30);

        constraints.gridy = 1;
        addSpec.add(adminDetailsLabel, constraints);

        constraints.insets = new Insets(10,10,10,10);
        constraints.gridy = 2;
        addSpec.add(usernameTextField, constraints);

        constraints.gridy = 3;
        addSpec.add(adminPasswordField, constraints);


        submitButton.setActionCommand(OK);
        submitButton.addActionListener(checkCredentials());

        constraints.insets = new Insets(30,30,30,30);
        constraints.gridy = 4;
        addSpec.add(submitButton, constraints);


        addSpec.setVisible(true);
        mainPanel.add(addSpec, ADD_SPECTACLE);

    }

    private void setAdminPasswordFieldDetails() {

        adminPasswordField.setActionCommand(OK);
        adminPasswordField.addActionListener(checkCredentials());
        adminPasswordField.setPreferredSize(new Dimension(200, 30));
        adminPasswordField.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {

                String adminPass = getPassword();


                if(adminPass.equals(ENTER_PASSWORD)){
                    adminPasswordField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {

                String adminPass = getPassword();

                if(adminPass.isEmpty()){
                    adminPasswordField.setText(ENTER_PASSWORD);
                }

            }
        });
    }

    @NotNull
    private String getPassword() {
        char[] password = adminPasswordField.getPassword();
        StringBuilder sb = new StringBuilder();
        for (char c : password) {
            sb.append(c);
        }

        return sb.toString();
    }

    @NotNull
    private ActionListener checkCredentials() {
        return ev->{
            String cmd = ev.getActionCommand();

            String adminPass = getPassword();
            String[] types = {"Movie", "Theatre", "Cancel"};
            if (OK.equals(cmd)) { //Process the password.
                char[] input = adminPasswordField.getPassword();
                if (adminPass.equals("123456789")) {
                    int returnValue = JOptionPane.showOptionDialog(mainFrame, "What do you want to add?", "Add spectacle",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, types, types[0]);
                    System.out.println(returnValue);
                    if (returnValue == 0){
                        createMoviePanel();
                        CardLayout cl = (CardLayout)mainPanel.getLayout();
                        cl.show(mainPanel, MOVIES);
                    }
                    else if(returnValue == 1){
                        createTheatrePanel();
                        CardLayout cl = (CardLayout)mainPanel.getLayout();
                        cl.show(mainPanel,  THEATRES);
                    }
                    else{
                        System.out.println("dismiss");
                    }
                } else {
                    JOptionPane.showMessageDialog(mainFrame,
                            "Invalid password. Try again.",
                            "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                }

                //Zero out the possible password, for security.
                Arrays.fill(input, '0');

                adminPasswordField.selectAll();
                adminPasswordField.requestFocusInWindow();
            }
        };
    }

    private void createClientPanel() {

        clientNameTextField = new JTextField(NAME);
        areYouVipLabel = new JLabel("Are you VIP?");
        vipCheckBox = new JCheckBox("YES");
        notVipCheckBox = new JCheckBox("NO");
        submitClientButton = new JButton("Submit Client");

        JPanel client = new JPanel();
        client.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(20,20,20,20);

        constraints.gridy = 1;
        setClientNameTextFieldDetails();
        client.add(clientNameTextField, constraints);


        vipCheckBox.setActionCommand(vipCheckBox.getText());
        notVipCheckBox.setActionCommand(notVipCheckBox.getText());
        buttonGroup = new ButtonGroup();
        buttonGroup.add(vipCheckBox);
        buttonGroup.add(notVipCheckBox);

        constraints.gridy = 2;
        constraints.insets = new Insets(10,10,10,10);
        client.add(areYouVipLabel, constraints);

        constraints.gridy = 3;
        client.add(vipCheckBox, constraints);

        constraints.gridy = 4;
        client.add(notVipCheckBox, constraints);

        constraints.insets = new Insets(20,20,20,20);
        constraints.gridy = 5;
        client.add(selectSpectacleButton, constraints);

        setShowToPayButtonDetails();
        constraints.gridy = 6;
        client.add(showToPayButton, constraints);

        constraints.gridy = 7;
        setSubmitClientButtonDetails();
        client.add(submitClientButton, constraints);

        client.setVisible(true);
        client.setName(CLIENT);
        mainPanel.add(client, CLIENT);

    }

    private void setShowToPayButtonDetails() {
        showToPayButton = new JButton("Check tickets price");
        showToPayButton.setPreferredSize(new Dimension(200, 50));
        showToPayButton.setEnabled(false);
        showToPayButton.addActionListener(ev ->{
            JOptionPane.showMessageDialog(mainFrame, "You have to pay " + serviceAPI.getTotalToPay(),
                    "Total to pay",
                    JOptionPane.INFORMATION_MESSAGE);
        });

    }

    private void createButtonPanel() {
        buttons.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(10, 10, 10,10);


        constraints.gridy = 1;
        buttons.add(addClientButton,constraints);
        constraints.gridy = 2;
        buttons.add(selectSpectacleButton,constraints);
        constraints.gridy = 3;
        buttons.add(addSpectacleButton,constraints);

        buttons.setName(ACTIONS);
        mainPanel.add(buttons, ACTIONS);
    }

    private void setSelectSpectacleButtonDetails() {
        selectSpectacleButton.setVerticalAlignment(SwingConstants.CENTER);
        selectSpectacleButton.setPreferredSize(new Dimension(200, 50));
        selectSpectacleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout)(mainPanel.getLayout());
                cl.show(mainPanel, SPECTACLES);
            }
        });
    }

    private void setAddClientButtonDetails() {
        addClientButton.setVerticalAlignment(SwingConstants.CENTER);
        addClientButton.setPreferredSize(new Dimension(300, 50));
        addClientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout)mainPanel.getLayout();
                lastPanel = CLIENT;
                cl.show(mainPanel, CLIENT);
            }
        });
    }

    private void setClientNameTextFieldDetails() {
        clientNameTextField.setFont(clientNameTextField.getFont().deriveFont(17.0f));
        clientNameTextField.setForeground(Color.GRAY);
        clientNameTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (clientNameTextField.getText().equals(NAME)) {
                    clientNameTextField.setText("");
                    clientNameTextField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (clientNameTextField.getText().isEmpty()) {
                    clientNameTextField.setForeground(Color.GRAY);
                    clientNameTextField.setText(NAME);
                }
            }
        });
    }

    private void setAddSpectacleButtonDetails() {
        addSpectacleButton.setVerticalAlignment(SwingConstants.CENTER);
        addSpectacleButton.setPreferredSize(new Dimension(300, 50));
        addSpectacleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) mainPanel.getLayout();
                lastPanel = ADD_SPECTACLE;
                cl.show(mainPanel, ADD_SPECTACLE);
            }
        });
    }

    private void setTitle() {
        title.setBorder(new EmptyBorder(20, 20, 20, 20));
        title.setFont(title.getFont().deriveFont(30.0f));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        mainFrame.add(title, BorderLayout.NORTH);
    }

    private void createNavPanel() {

        JPanel navigationPanel = new JPanel();
        navigationPanel.add(new JButton(new AbstractAction("\u22b2Back") {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) mainPanel.getLayout();
                cl.first(mainPanel);
            }
        }));

        navigationPanel.add(new JButton(new AbstractAction("Next\u22b3") {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) mainPanel.getLayout();
                if(lastPanel != null){
                    cl.show(mainPanel, lastPanel);
                }
            }
        }));

        navigationPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        navigationPanel.setPreferredSize(new Dimension(50,50));
        mainFrame.add(navigationPanel, BorderLayout.SOUTH);

    }

    private void setMainFrame() {
        mainFrame.setSize(650, 650);
        mainFrame.setResizable(false);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());
    }

    private void createSpectaclePanel() {
        int numOfSpecs = serviceAPI.numberOfSpectacles();
        List<String> specsNum = serviceAPI.getSpectaclesName();

        JPanel spectaclePanel = new JPanel();
        spectaclePanel.setLayout(new GridLayout(numOfSpecs, 1));

        for(int i = 0; i < numOfSpecs; i++){

            JButton button = new JButton();

            button.setAction(spectacleAction(i));
            spectaclePanel.add(button);
            button.setText(specsNum.get(i));
        }

        spectaclePanel.setName(SPECTACLES);
        spectaclePanel.setVisible(true);
        mainPanel.add(spectaclePanel, SPECTACLES);
    }

    @NotNull
    private AbstractAction spectacleAction(int index) {
        return new AbstractAction() {


            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame seatsFrame = new JFrame();
                seatsFrame.setResizable(false);
                seatsFrame.setSize(500, 500);
                seatsFrame.setLocationRelativeTo(null);
                seatsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                seatsFrame.setLayout(new BorderLayout(10, 10));


                serviceAPI.selectSpectacle(index + 1);

                Spectacle spectacle = serviceAPI.getSelectedSpectacle(index);
                List<Seat> seatList = serviceAPI.getSpectacleSeats(index);

                int nrSeats = spectacle.getNrSeats() + spectacle.getNrVipSeats();
                int[][] seatsNumbers = new int[nrSeats][5];
                int counter = 0;


                JPanel seats = new JPanel();
                seats.setSize(350, 350);

                seats.setLayout(new GridLayout(nrSeats/5, 5));
                selectedSeats = new ArrayList<>();

                for (int row = 0; row < nrSeats/5; row++){
                    for(int col = 0; col < 5; col++){
                        JButton seat = new JButton(seatList.get(counter).getNumber() + "");
                        seat.putClientProperty("isPressed", false);
                        seatsNumbers[row][col] = counter + 1;
                        if (seatList.get(counter).isOccupied()){
                            seat.setBackground(new Color(255,30,30));
                            seat.setEnabled(false);
                        } else{
                            seat.setBackground(new Color(10,255,0));

                            if (seatList.get(counter).getType().equals("Armchair")){
                                seat.setBackground(new Color(40,140,40));
                            }
                        }

                        seat.addActionListener(seatActionListener(seatList, seat, seatsNumbers[row][col]));
                        counter++;
                        seats.add(seat);
                    }
                }

                seats.setVisible(true);

                JButton scene = new JButton("Scene");
                scene.setEnabled(false);
                scene.setMargin(new Insets(20, 20, 20, 20));

                scene.setBackground(new Color(100,100,100));

                submitSeatsButton = new JButton("Submit Seats");
                submitSeatsButton.setMargin(new Insets(20,20,20,20));
                submitSeatsButton.addActionListener(event->{
                    showToPayButton.setEnabled(true);
                    seatsFrame.dispose();
                    System.out.println(selectedSeats);
                    CardLayout cl = (CardLayout)mainPanel.getLayout();
                    cl.show(mainPanel, CLIENT);
                });



                seatsFrame.add(scene, BorderLayout.NORTH);
                seatsFrame.add(seats, BorderLayout.CENTER);
                seatsFrame.add(submitSeatsButton, BorderLayout.SOUTH);
                seatsFrame.setVisible(true);
            }
        };
    }

    private void setSubmitClientButtonDetails() {
        submitClientButton = new JButton("Submit client");
        submitClientButton.setMargin(new Insets(10,10,10,10));
        submitClientButton.addActionListener(event->{
            Client client;

            try {
                client = serviceAPI.createClient(clientNameTextField.getText(), buttonGroup.getSelection().getActionCommand(), selectedSeats);
                System.out.println(client);
                JOptionPane.showMessageDialog(mainFrame, "Client Successfully added");

                CardLayout cl = (CardLayout) mainPanel.getLayout();
                cl.show(mainPanel, ACTIONS);

            }catch (NoNameException ex){
                JOptionPane.showMessageDialog(mainFrame, "No name Provided", "Error", JOptionPane.ERROR_MESSAGE);
            }catch (TooManySeatsException ex){
                JOptionPane.showMessageDialog(mainFrame, "Too many seats selected", "Error", JOptionPane.ERROR_MESSAGE);
            }catch (OccupiedSeatException ex){

                String message;

                if(buttonGroup.getSelection().getActionCommand().equals("YES")){
                    message = "Non VIP person";
                }else{
                    message = "VIP person";
                }

                JOptionPane.showMessageDialog(mainFrame, "Wrong seat selected. Seat is for " + message,
                        "Error", JOptionPane.ERROR_MESSAGE);
            }


        });
    }

    @NotNull
    private ActionListener seatActionListener(List<Seat> seatList, JButton seat, int counter) {
        return event->{

            if (!(boolean ) seat.getClientProperty("isPressed")){
                seat.setBackground(new Color(152, 251, 152));
                seat.putClientProperty("isPressed", true);
                selectedSeats.add(counter);
            }
            else{
                seat.setBackground(new Color(10,255,0));

                if (seatList.get(counter).getType().equals("Armchair")){
                    seat.setBackground(new Color(40,140,40));
                }
                seat.putClientProperty("isPressed", false);
                selectedSeats.remove(Integer.valueOf(counter));

            }


        };
    }

}
