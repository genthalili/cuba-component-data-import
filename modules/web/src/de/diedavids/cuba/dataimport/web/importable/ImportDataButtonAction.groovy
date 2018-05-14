package de.diedavids.cuba.dataimport.web.importable

import com.haulmont.cuba.gui.WindowManager
import com.haulmont.cuba.gui.components.Action
import com.haulmont.cuba.gui.components.Component
import com.haulmont.cuba.gui.components.ListComponent
import com.haulmont.cuba.gui.components.actions.ItemTrackingAction

class ImportDataButtonAction extends ItemTrackingAction implements Action.HasOpenType {

    WindowManager.OpenType openType

    static final String ACTION_ID = 'importDataAction'

    ImportDataButtonAction(ListComponent target) {
        this(ACTION_ID, target)
    }

    ImportDataButtonAction(String id, ListComponent target) {
        super(target, id)
        icon = 'font-icon:COMMENTS_O'
        caption = 'Import'
    }

    @Override
    void actionPerform(Component component) {
        super.actionPerform(component)
        target.frame.showNotification('TODO: Import Action')
    }
}
