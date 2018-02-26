package com.cigniti.JIRA;

import java.util.Collections;

import javax.swing.DefaultListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class MyListDataListener implements ListDataListener {

	@Override
	public void contentsChanged(ListDataEvent arg0) {

	}

	@Override
	public void intervalAdded(ListDataEvent arg0) {

		@SuppressWarnings("rawtypes")
		DefaultListModel model = (DefaultListModel) arg0.getSource();
	    if(model.getSize() == 0)
			ImportTestCases.btnValidate.setEnabled(true);	    	
		else
		{
			ImportTestCases.btnValidate.setEnabled(false);
	    	ImportTestCases.lblCheckmark.setVisible(false);
	    	ImportTestCases.lblValidationmessage.setVisible(false);
		}
	}

	@Override
	public void intervalRemoved(ListDataEvent arg0) {

		@SuppressWarnings("rawtypes")
		DefaultListModel model = (DefaultListModel) arg0.getSource();
		if(model.getSize() == 0)
			ImportTestCases.btnValidate.setEnabled(true);
		else
		{
			ImportTestCases.btnValidate.setEnabled(false);
			ImportTestCases.lblCheckmark.setVisible(false);
			ImportTestCases.lblValidationmessage.setVisible(false);
		}
		
	}

}
