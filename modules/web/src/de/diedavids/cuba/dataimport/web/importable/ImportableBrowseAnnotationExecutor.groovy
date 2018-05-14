package de.diedavids.cuba.dataimport.web.importable

import com.haulmont.cuba.gui.components.Button
import com.haulmont.cuba.gui.components.ButtonsPanel
import com.haulmont.cuba.gui.components.ListComponent
import com.haulmont.cuba.gui.components.Window
import de.balvi.cuba.declarativecontrollers.web.annotationexecutor.browse.BrowseAnnotationExecutor
import de.balvi.cuba.declarativecontrollers.web.helper.ButtonsPanelHelper
import org.springframework.stereotype.Component

import javax.inject.Inject
import java.lang.annotation.Annotation

@Component
class ImportableBrowseAnnotationExecutor implements BrowseAnnotationExecutor<Importable> {

    @Inject
    ButtonsPanelHelper buttonsPanelHelper

    @Override
    void init(Importable annotation, Window.Lookup browse, Map<String, Object> params) {
        ListComponent listComponent = browse.getComponent(annotation.listComponent()) as ListComponent
        def action = new ImportDataButtonAction(listComponent)
        listComponent.addAction(action)
        if (annotation.buttonsPanel()) {
            ButtonsPanel buttonsPanel = browse.getComponent(annotation.buttonsPanel()) as ButtonsPanel
            Button dokumentButton = buttonsPanelHelper.createButton(annotation.buttonId(), buttonsPanel, [])
            dokumentButton.action = action
        }
    }

    @Override
    boolean supports(Annotation annotation) {
        annotation instanceof Importable
    }


    @Override
    void ready(Importable annotation, Window.Lookup browse, Map<String, Object> params) {

    }
}
