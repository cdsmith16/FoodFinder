package com.example.view;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;

import com.example.models.FoodHandler;


public class FoodFrame  extends JFrame implements MouseListener{

	JTextArea input;
	private BorderLayout borderLayout;
	private Box box;
	JButton fromLatLng;
	JButton fromAddress;
	Document doc;
	FoodHandler handler;
	JTextArea result;
	
	public FoodFrame() {
		super();
		box = new Box(BoxLayout.LINE_AXIS);
		box.setBorder(new TitledBorder("FindFood"));
		fromAddress = new JButton("From Address");
		fromAddress.addMouseListener(this);
		
		fromLatLng = new JButton("From Latitude&Longitude");
		fromLatLng.addMouseListener(this);
		
		box.add(fromAddress, BoxLayout.X_AXIS);
		box.add(fromLatLng, BoxLayout.X_AXIS);
		
		 input = new JTextArea(5,5);
		 input.setBorder(new TitledBorder("Input"));
		 input.setText("37.7921195,-122.4038279");
		 input.getDocument().addDocumentListener(new DocumentListener(){
			public void insertUpdate(DocumentEvent arg0) {
				doc = arg0.getDocument();
				return;
			}
			
			//doing nothing
			public void removeUpdate(DocumentEvent arg0) {
				return;
			}
			public void changedUpdate(DocumentEvent arg0) {
				return;
			}
			 
		 });
		 
		result = new JTextArea(5,5);
		result.setBorder(new TitledBorder("Results"));
		
		add(input,BorderLayout.NORTH);
		add(box);
		add(result, BorderLayout.SOUTH);
		// Could do this:
		setLocationByPlatform(true);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	public void mouseClicked(MouseEvent arg0) {
		String text = "";
		try {
			Document doc = this.input.getDocument();
			text = doc.getText(0, doc.getLength());
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(arg0.getButton() == MouseEvent.BUTTON1){
			String lat = text.substring(0,text.indexOf(','));
			String lng = text.substring(text.indexOf(',')+1,text.length()-1);
	        handler = new FoodHandler(Double.parseDouble(lat), Double.parseDouble(lng));
		} else{
			handler = new FoodHandler(text);
		}
		
        StringBuffer buffer = new StringBuffer();
        JSONArray foods;

		try {
			foods = handler.getFoods();
			for(int i=0; i<foods.length(); i++){
				System.out.println(foods.getString(i));
				buffer.append(foods.getString(i)+"\n");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
        result.setText(buffer.toString());
		
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param args
	 */

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }
		
		FoodFrame frame = new FoodFrame();
		
	}
}
