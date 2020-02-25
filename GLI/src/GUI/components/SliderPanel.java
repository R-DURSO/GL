package GUI.components;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SliderPanel extends JPanel {
	private static final long serialVersionUID = -8099360319313148061L;
	private JLabel title;
	private JLabel value;
	private JSlider slider;
	
	public SliderPanel(String title, int maxValue, int minValue, int initialValue) {
		this.title = new JLabel(title, SwingConstants.CENTER);
		this.value = new JLabel("" + initialValue, SwingConstants.CENTER);
		this.slider = new JSlider(minValue, maxValue, initialValue);
		init();
	}

	private void init() {
		setLayout(new GridLayout(0,1));
		add(title);
		add(value);
		
		//slider preferences
		slider.setMajorTickSpacing(5);
		slider.setMinorTickSpacing(1);
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);
		slider.addChangeListener(new ChangeSliderValue());
		
		add(slider);
	}
	
	public int getValue() {
		return slider.getValue();
	}
	
	class ChangeSliderValue implements ChangeListener{
		@Override
		public void stateChanged(ChangeEvent e) {
			value.setText("" + slider.getValue());
		}
		
	}

}
