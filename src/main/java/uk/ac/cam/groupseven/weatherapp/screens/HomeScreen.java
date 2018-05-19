package uk.ac.cam.groupseven.weatherapp.screens;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import hu.akarnokd.rxjava2.swing.SwingObservable;
import hu.akarnokd.rxjava2.swing.SwingSchedulers;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.schedulers.Schedulers;
import uk.ac.cam.groupseven.weatherapp.LoadingIcon;
import uk.ac.cam.groupseven.weatherapp.Screen;
import uk.ac.cam.groupseven.weatherapp.ScreenLayout;
import uk.ac.cam.groupseven.weatherapp.models.FlagStatus;
import uk.ac.cam.groupseven.weatherapp.styles.ApplyStyle;
import uk.ac.cam.groupseven.weatherapp.styles.BackgroundStyle;
import uk.ac.cam.groupseven.weatherapp.styles.ButtonStyle;
import uk.ac.cam.groupseven.weatherapp.styles.CenterTextStyle;
import uk.ac.cam.groupseven.weatherapp.viewmodels.HomeViewModel;
import uk.ac.cam.groupseven.weatherapp.viewmodels.Loadable;
import uk.ac.cam.groupseven.weatherapp.viewmodelsources.ViewModelSource;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class HomeScreen implements Screen {
    @Inject
    ViewModelSource<Loadable<HomeViewModel>> homeWeatherSource;
    @Inject
    ViewModelSource<ImageIcon> crestImageSource;
    @ApplyStyle(BackgroundStyle.class)
    private JPanel panel;

    @ApplyStyle(CenterTextStyle.class)
    private JTextPane flagText;
    @ApplyStyle(ButtonStyle.class)
    private JButton refreshButton;
    @ApplyStyle(ButtonStyle.class)
    private JButton crestButton;
    @ApplyStyle(ButtonStyle.class)
    private JButton leftButton;
    @ApplyStyle(ButtonStyle.class)
    private JButton rightButton;
    @ApplyStyle(ButtonStyle.class)

    private JButton additionalInformationButton;
    @ApplyStyle(BackgroundStyle.class)
    private JPanel topPanel;
    @ApplyStyle(BackgroundStyle.class)
    private JPanel midPanel;
    @ApplyStyle(BackgroundStyle.class)
    private JPanel bottomPanel;
    @ApplyStyle(BackgroundStyle.class)
    private JLabel tempIcon;

    @ApplyStyle(CenterTextStyle.class)
    private JTextPane tempText;
    @ApplyStyle(CenterTextStyle.class)
    private JTextPane windText;
    @ApplyStyle(BackgroundStyle.class)
    private JLabel windIcon;
    @ApplyStyle(ButtonStyle.class)
    private JPanel weatherPanel;
    @ApplyStyle(BackgroundStyle.class)
    private JLabel flagIcon;
    @Inject
    private LoadingIcon loadingIcon;
    private boolean loadingAnimating = false;
    private Disposable loadingDisposable = EmptyDisposable.INSTANCE;
    @Inject
    @Named("tempBigIcon")
    private ImageIcon thermometer;
    @Inject
    @Named("windBigIcon")
    private ImageIcon wind;

    public JPanel getPanel() {
        return panel;
    }

    @Override
    public Disposable start() {
        // Set temp icon
        tempIcon.setIcon(thermometer);
        //Set wind icon
        windIcon.setIcon(wind);
        refreshButton.setIcon(loadingIcon);
        setLoading(true);
        crestImageSource.getViewModel((getRefreshObservable())).subscribe(this::updateCrest);
        return
                homeWeatherSource
                        .getViewModel(getRefreshObservable())
                        .subscribe(this::updateScreen);


    }

    private void updateScreen(Loadable<HomeViewModel> viewModelLoadable) {
        HomeViewModel viewModel = viewModelLoadable.getViewModel();

        if (viewModelLoadable.getLoading()) {
            flagText.setText("loading");
            tempText.setText("Temperature: ...");
            windText.setText("Wind Speed: ...");
            setLoading(true);

        } else if (viewModelLoadable.getError() != null) {
            setLoading(false);
            flagText.setText("Error");
            windText.setText("Error");
            tempText.setText("Error");
        } else {
            setLoading(false);
            // Get weather info and set text
            FlagStatus flagStatus = viewModel.getFlag();
            flagText.setText("Flag: " + flagStatus.getDisplayName());
            tempText.setText("Temperature: " + Float.toString(viewModel.getTemperature()));
            windText.setText("Wind Speed: " + Float.toString(viewModel.getWindSpeed()));
            flagIcon.setIcon(viewModel.getFlagImage());

        }

    }

    private void updateCrest(ImageIcon viewModel) {
        crestButton.setIcon(viewModel);
    }

    public Observable<ScreenLayout.Direction> getScreenChanges() {
        return SwingObservable.actions(leftButton).map(x -> ScreenLayout.Direction.LEFT)
                .mergeWith(SwingObservable.actions(rightButton).map(x -> ScreenLayout.Direction.RIGHT))
                .mergeWith(SwingObservable.actions(crestButton).map(x -> ScreenLayout.Direction.UP))
                .mergeWith(SwingObservable.actions(additionalInformationButton).map(x -> ScreenLayout.Direction.DOWN));

    }

    private Observable<Object> getRefreshObservable() {
        return
                Observable.just(new Object()) //Refresh immediately
                        .concatWith(SwingObservable.actions(refreshButton))//Refresh when button pressed
                        .mergeWith(Observable.interval(15, TimeUnit.SECONDS)); //Refresh every 15 seconds
    }

    private void setLoading(boolean loading) {
        loadingDisposable.dispose();
        int startRotation = loadingIcon.getRotation();
        Disposable loadingObservable;
        if (loading) {
            loadingAnimating = true;
            loadingObservable = Observable
                    .intervalRange(0, 360, 0, 10, TimeUnit.MILLISECONDS)
                    .repeat().subscribeOn(Schedulers.computation())
                    .subscribe(x -> loadingIcon.setRotation(startRotation + Math.toIntExact(x) * 10),
                            Throwable::printStackTrace,
                            () -> loadingAnimating = false);


        } else {
            loadingAnimating = true;
            loadingObservable = Observable
                    .intervalRange(startRotation / 10, (360 - startRotation) / 10, 0, 10, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.computation())
                    .subscribe(x -> loadingIcon.setRotation(Math.toIntExact(x) * 10),
                            Throwable::printStackTrace,
                            () -> loadingAnimating = false);
        }

        Disposable repaintDisposable = Observable
                .interval(0, 10, TimeUnit.MILLISECONDS)
                .takeWhile(x -> loadingAnimating)
                .observeOn(SwingSchedulers.edt())
                .subscribe(x -> refreshButton.repaint());

        loadingDisposable = new CompositeDisposable(loadingObservable, repaintDisposable);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel = new JPanel();
        panel.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel.setMinimumSize(new Dimension(500, 300));
        panel.setPreferredSize(new Dimension(500, 300));
        panel.setRequestFocusEnabled(false);
        topPanel = new JPanel();
        topPanel.setLayout(new GridLayoutManager(1, 3, new Insets(10, 10, 0, 10), -1, -1));
        topPanel.setAlignmentX(0.5f);
        topPanel.setAlignmentY(0.5f);
        topPanel.setAutoscrolls(false);
        topPanel.setVisible(true);
        panel.add(topPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, new Dimension(150, 200), new Dimension(-1, 200), 1, false));
        crestButton = new JButton();
        crestButton.setText("");
        topPanel.add(crestButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        refreshButton = new JButton();
        refreshButton.setText("");
        topPanel.add(refreshButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(60, 60), null, 0, false));
        final Spacer spacer1 = new Spacer();
        topPanel.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        midPanel = new JPanel();
        midPanel.setLayout(new GridLayoutManager(1, 3, new Insets(0, 10, 0, 10), -1, -1));
        panel.add(midPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, new Dimension(-1, 300), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.setOpaque(false);
        panel1.setVisible(true);
        midPanel.add(panel1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        rightButton = new JButton();
        rightButton.setActionCommand(ResourceBundle.getBundle("strings").getString("24.hour"));
        rightButton.setHideActionText(false);
        rightButton.setLabel("24 Hour ⇒");
        this.$$$loadButtonText$$$(rightButton, ResourceBundle.getBundle("strings").getString("24.hour1"));
        panel1.add(rightButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 50), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.setOpaque(false);
        midPanel.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        leftButton = new JButton();
        leftButton.setActionCommand(ResourceBundle.getBundle("strings").getString("7.day"));
        this.$$$loadButtonText$$$(leftButton, ResourceBundle.getBundle("strings").getString("7.day1"));
        panel2.add(leftButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 50), null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.setOpaque(false);
        midPanel.add(panel3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        flagText = new JTextPane();
        flagText.setEditable(false);
        flagText.setText("");
        panel3.add(flagText, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(100, 50), null, 0, false));
        flagIcon = new JLabel();
        flagIcon.setAlignmentX(0.5f);
        flagIcon.setOpaque(true);
        flagIcon.setText("");
        panel3.add(flagIcon, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(0, 0), new Dimension(250, 250), null, 0, false));
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 20, 0), -1, -1));
        panel.add(bottomPanel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        additionalInformationButton = new JButton();
        this.$$$loadButtonText$$$(additionalInformationButton, ResourceBundle.getBundle("strings").getString("more.info"));
        bottomPanel.add(additionalInformationButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 50), null, 0, false));
        final Spacer spacer2 = new Spacer();
        bottomPanel.add(spacer2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        bottomPanel.add(spacer3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 1, new Insets(0, 100, 0, 100), -1, -1));
        panel4.setOpaque(false);
        panel.add(panel4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        weatherPanel = new JPanel();
        weatherPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(weatherPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 3, new Insets(0, 20, 0, 20), -1, -1));
        panel5.setOpaque(false);
        weatherPanel.add(panel5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        tempText = new JTextPane();
        tempText.setEditable(false);
        tempText.setText("");
        panel5.add(tempText, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 72), null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel5.add(spacer4, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, new Dimension(14, 72), null, 0, false));
        tempIcon = new JLabel();
        tempIcon.setText("");
        panel5.add(tempIcon, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, 72), null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 3, new Insets(0, 20, 0, 20), -1, -1));
        panel6.setOpaque(false);
        weatherPanel.add(panel6, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel6.add(spacer5, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        windText = new JTextPane();
        windText.setEditable(false);
        panel6.add(windText, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        windIcon = new JLabel();
        windIcon.setText("");
        panel6.add(windIcon, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, 100), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadButtonText$$$(AbstractButton component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }
}
