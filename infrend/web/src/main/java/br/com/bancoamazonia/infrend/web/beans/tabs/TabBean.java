package br.com.bancoamazonia.infrend.web.beans.tabs;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.faces.bean.ManagedBean;

import org.primefaces.event.TabChangeEvent;
/**
 * BackBean responsavel pelo gerenciamento das abas ativas
 * @author Paulo Rudolph (7485)
 *
 */
@ManagedBean
public class TabBean
{
	public enum Tab
	{
		LIST("lista"),
		EDIT("editar"),
		CREATE("criar");
		
		private Tab(String title)
		{
			this.title = title;
		}
		
		private String title;
		public String getTitle()
		{
			return title;
		}
		
		public String toString()
		{
			return toString().toLowerCase();
		}
		
		public static Tab find(String title)
		{
			Set<Tab> enuns = EnumSet.allOf(Tab.class);
			Tab tab = null;
			for(Tab t : enuns)
				if(t.title.equalsIgnoreCase(title))
				{
					tab = t;
					break;
				}
			return tab;
		}
		
	}
	
	private int tabIndex = 0;
	public int getTabIndex()
	{
		return tabIndex;
	}
	public void setTabIndex(int tabIndex)
	{
		this.tabIndex = tabIndex;
	}
	public void onTabChange(TabChangeEvent event)
	{
		tabIndex = TabBean.Tab.find(event.getTab().getTitle()).ordinal();
	}
	public List<TabBean.Tab> getTabs()
	{
		List<TabBean.Tab> tabs = new ArrayList<TabBean.Tab>(EnumSet.allOf(TabBean.Tab.class));
		return tabs;
	}
	
}
