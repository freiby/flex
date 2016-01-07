package com.wxxr.nirvana.inspinia;

import java.io.IOException;

import javax.servlet.jsp.PageContext;

import com.wxxr.nirvana.ContainerAccess;
import com.wxxr.nirvana.IUIRenderContext;
import com.wxxr.nirvana.exception.NirvanaException;
import com.wxxr.nirvana.util.JspUtil;
import com.wxxr.nirvana.workbench.IRenderContext;
import com.wxxr.nirvana.workbench.IView;
import com.wxxr.nirvana.workbench.impl.CommonRender;
import com.wxxr.nirvana.workbench.impl.UIComponent;
import com.wxxr.nirvana.workbench.impl.View;
import com.wxxr.nirvana.workbench.impl.WorkbenchPage.ViewRef;

public class PageLayoutRender  extends CommonRender{
	private String uri = "view/dynmicLayout.jsp";
	public void render(UIComponent ui, IRenderContext context)
			throws NirvanaException {
		ViewRef[] views = ContainerAccess.getSessionWorkbench().getCurrentPage().getAllViewRefs();
		int size = views.length;
		boolean isDivisible = (size % Column.COUNT) == 0;
		int rs = 0;
        if (isDivisible) {
        	rs = size / Column.COUNT;
        } else {
        	rs = size / Column.COUNT + 1;
        }
		
		Row[] rows = new Row[rs];
		int v = 0;
		for(int i=0; i<rows.length; i++){
			Row r = new Row();
			rows[i] = r;
			int js = size % Column.COUNT;
			int count = Column.COUNT;
			if(js != 0){
				if(i == rows.length-1){
					count = js;
				}
			}
			for(int j=0; j<count; j++){
				Column c = new Column();
				ViewRef vr = views[v];
				IView view = (IView) ContainerAccess.getSessionWorkbench().getCurrentPage().getViewsById(vr.getId());
				c.setViewUrl(JspUtil.getRealPath(view.getContributorId(),
						view.getContributorVersion(), view.getURI()));
				c.setViewId(view.getUniqueIndentifier());
				r.addColumn(c);
				v++;
			}
		}
		String pageID = ContainerAccess.getSessionWorkbench().getCurrentPage().getUniqueIndentifier();
		context.get(IUIRenderContext.class).getRequestContext().getPageContext().setAttribute("pageId", pageID, PageContext.REQUEST_SCOPE);
		context.get(IUIRenderContext.class).getRequestContext().getPageContext().setAttribute("rows", rows, PageContext.REQUEST_SCOPE);
		try {
			context.get(IUIRenderContext.class).getRequestContext().dispatch(JspUtil.getRealPath(ui.getContributorId(),
					ui.getContributorVersion(), uri));
		} catch (IOException e) {
			throw new NirvanaException(e);
		}
	}
}
