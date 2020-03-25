package GUI.components.game;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import GUI.components.GuiPreferences;
import GUI.sub_panels.GamePanel;
import data.Position;
import data.Power;
import data.boxes.Box;

public class InfosPanel extends JPanel{
	private BoxSelectedPanel boxSelectedPanel;
	
	private JPanel orderPanel = new JPanel();
	private GamePanel context;
	
	private JPanel statsPanel = new JPanel();
	private Power powers[];
	private ChartPanel chartPanel;
	private final Dimension CHART_DIMENSION = new Dimension(GuiPreferences.WIDTH / 5, GuiPreferences.HEIGHT / 3);	
	public InfosPanel() {
		setLayout(new GridLayout(0,1));
	}
	
	public void initInfosPanel(GamePanel context, Power powers[]) {
		this.context = context;
		this.powers = powers;
		boxSelectedPanel = new BoxSelectedPanel(context.getMapPanel().getBoxByPosition(new Position(0,0)));
		add(boxSelectedPanel);
		add(statsPanel);
	}
	
	public void refreshBoxHover(Box box) {
		boxSelectedPanel.refresh(box);
	}
	
	public void refreshStatsPanel() {
		chartPanel = new ChartPanel(setBarChart());
		chartPanel.setPreferredSize(CHART_DIMENSION);
		statsPanel.removeAll();
		statsPanel.add(chartPanel);
		repaint();
	}
	
	private JFreeChart setBarChart() {
		DefaultPieDataset dataset = new DefaultPieDataset();
		for(int i = 0; i < powers.length; i++){
			int territorySize = powers[i].getTerritory().size();
			dataset.setValue(powers[i].getName() + " : " + territorySize, territorySize);
		}
		
		return ChartFactory.createPieChart("Territoires Possédés", dataset, false, false, false);
	}
}
