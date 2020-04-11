package GUI.components.game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

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
import data.PowerStats;
import data.boxes.Box;

public class InfosPanel extends JPanel {
	private BoxSelectedPanel boxSelectedPanel;

	private GamePanel gamePanel;

	private JPanel statsPanel = new JPanel();
	private Power powers[];
	private ChartPanel generalChartPanel;
	private ChartPanel territoryChartPanel;
	
	private final Dimension DIM_INFOS = GamePanel.DIM_INFOS;
	private final Dimension DIM_SELECTION = new Dimension((int)DIM_INFOS.getWidth(), (int)DIM_INFOS.getHeight() / 3);
	private final Dimension DIM_STATS = new Dimension((int)DIM_INFOS.getWidth(), 2 * (int)DIM_INFOS.getHeight() / 3);
	private final Dimension DIM_CHART = new Dimension((int)DIM_STATS.getWidth(), (int)DIM_STATS.getHeight() / 2);

	public InfosPanel() {
		setLayout(new BorderLayout());
	}

	public void initInfosPanel(GamePanel context, Power powers[]) {
		this.gamePanel = context;
		this.powers = powers;
		
		// select first box on map for box selectionPanel
		MapPanel mapPanel = gamePanel.getMapPanel();
		Box firstBox = mapPanel.getBoxByPosition(new Position(0, 0));
		
		boxSelectedPanel = new BoxSelectedPanel(firstBox);
		boxSelectedPanel.setPreferredSize(DIM_SELECTION);
		add(boxSelectedPanel, BorderLayout.NORTH);

		// set Stats panel, which contains... stats (and 2 ChartPanels)
		statsPanel.setLayout(new GridLayout(0,1));
		statsPanel.setPreferredSize(DIM_STATS);
		
		generalChartPanel = new ChartPanel(null);
		generalChartPanel.setPreferredSize(DIM_CHART);
		territoryChartPanel = new ChartPanel(null);
		territoryChartPanel.setPreferredSize(DIM_CHART);

		statsPanel.add(generalChartPanel);
		statsPanel.add(territoryChartPanel);

		add(statsPanel, BorderLayout.SOUTH);
		refreshStatsPanel();
	}

	/**
	 * Change display of selection panel depending on which box mouse is currently hover
	 * @param box the box which mouse cursor is on
	 */
	public void refreshBoxHover(Box box) {
		boxSelectedPanel.refresh(box);
	}

	/**
	 * Change stats display (this method has to be called during 
	 */
	public void refreshStatsPanel() {
		//put data in 2 graphs
		generalChartPanel.setChart(setGeneralChartPanel());
		generalChartPanel.repaint();
		territoryChartPanel.setChart(setTerritoryChartPanel());
		territoryChartPanel.repaint();
		repaint();
	}
	
	private JFreeChart setGeneralChartPanel() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		for(int i = 0; i < powers.length; i++) {
			int numberBuildings = powers[i].getNumberBuildings();
			int numberUnits = powers[i].getNumberUnits();
			
			dataset.addValue(numberUnits, powers[i].getName(), "Unités");
			dataset.addValue(numberBuildings, powers[i].getName(), "Batiments");
		}
		
		
		return ChartFactory.createBarChart("Statistiques générales", null, "Nombre", dataset, PlotOrientation.VERTICAL, true, true, false);
	}

	private JFreeChart setTerritoryChartPanel() {
		DefaultPieDataset dataset = new DefaultPieDataset();
		for(int i = 0; i < powers.length; i++) {
			int territorySize = powers[i].getTerritorySize();
			dataset.setValue(powers[i].getName() + " : " + territorySize, territorySize);
		}
		
		return ChartFactory.createPieChart("Territoires Possédés", dataset, false, false, false);
	}
}
