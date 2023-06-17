package presentationLayer.gui.plAbstracts;

import presentationLayer.gui.plUtils.Colors;
import presentationLayer.gui.plUtils.ContentPanel;
import presentationLayer.gui.plUtils.TextNote;

import java.awt.*;

public abstract class TransportBasePanel extends ScrollablePanel {

    protected TextNote text;

    public ContentPanel getContentPanel() {
        return contentPanel;
    }

    protected ContentPanel contentPanel;

    public TransportBasePanel() {
        super("src/resources/cartoon_truck.jpg");
        init();
    }

    public TransportBasePanel(String fileName) {
        super(fileName);
        init();
    }

    private void init() {
        contentPanel = new ContentPanel(Colors.getContentPanelColor());
        contentPanel.setLayout(new GridBagLayout());
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panel.add(contentPanel,gbc);
    }

    @Override
    public void componentResized(Dimension newSize) {
        super.componentResized(newSize);
        Dimension preferredSize = new Dimension((int) (panel.getWidth() * 0.8), (int) (panel.getHeight()*0.6));
        contentPanel.setPreferredSize(preferredSize);
        scrollPane.revalidate();
    }
}
