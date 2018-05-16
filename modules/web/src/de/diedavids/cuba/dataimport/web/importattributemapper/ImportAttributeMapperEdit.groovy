package de.diedavids.cuba.dataimport.web.importattributemapper

import com.haulmont.chile.core.model.MetaClass
import com.haulmont.cuba.core.global.Metadata
import com.haulmont.cuba.gui.components.*
import com.haulmont.cuba.gui.data.Datasource
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory
import de.diedavids.cuba.dataimport.converter.MetaPropertyMatcher
import de.diedavids.cuba.dataimport.entity.AttributeType
import de.diedavids.cuba.dataimport.entity.ImportAttributeMapper
import de.diedavids.cuba.dataimport.web.util.MetadataSelector

import javax.inject.Inject

class ImportAttributeMapperEdit extends AbstractEditor<ImportAttributeMapper> {

    @Inject
    Datasource<ImportAttributeMapper> importAttributeMapperDs

    @Inject
    Metadata metadata

    @Inject
    MetaPropertyMatcher metaPropertyMatcher

    @Inject
    private ComponentsFactory componentsFactory

    @Inject
    private FieldGroup fieldGroup

    @Inject
    private MetadataSelector metadataSelector


    private LookupField dynamicEntityAttribute
    private LookupField entityAttribute
    private LookupField associationEntityAttribute
    private LookupField associationLookupAttribute


    private static final String ENTITY_ATTRIBUTE_NAME = 'entityAttribute'
    private static final String ATTRIBUTE_TYPE_NAME = 'attributeType'
    private static final String ASSOCIATION_LOOKUP_NAME = 'associationLookupAttribute'
    private static final String DYNAMIC_ENTITY_ATTRIBUTE_NAME = 'dynamicEntityAttribute'

    @Override
    void init(Map<String, Object> params) {

        importAttributeMapperDs.addItemPropertyChangeListener(new Datasource.ItemPropertyChangeListener<ImportAttributeMapper>() {
            @Override
            void itemPropertyChanged(Datasource.ItemPropertyChangeEvent<ImportAttributeMapper> e) {
                if (e.property == ATTRIBUTE_TYPE_NAME) {
                    clearEntityAttributeValues()
                }
            }
        })


        importAttributeMapperDs.addItemPropertyChangeListener(new Datasource.ItemPropertyChangeListener<ImportAttributeMapper>() {
            @Override
            void itemPropertyChanged(Datasource.ItemPropertyChangeEvent<ImportAttributeMapper> e) {
                if (e.property == ENTITY_ATTRIBUTE_NAME && item.attributeType == AttributeType.ASSOCIATION_ATTRIBUTE) {
                    activateAssociationLookupAttributes(e.value as String)
                }
            }
        })
    }

    @Override
    protected void postInit() {
        initDynamicEntityAttributeLookup()
        initEntityAttributeLookup()
        initAssociationEntityAttributeLookup()
        initAssociationLookupAttributeLookup()
        initAttributeTypeOptionsGroup()
        showLookupFieldsForAttributeType(item.attributeType)
    }

    void clearEntityAttributeValues() {
        item.entityAttribute = null
        item.associationLookupAttribute = null
        item.dynamicEntityAttribute = null
    }

    def initAssociationEntityAttributeLookup() {
        FieldGroup.FieldConfig fieldConfig = fieldGroup.getField('associationEntityAttribute')
        associationEntityAttribute = componentsFactory.createComponent(LookupField)
        associationEntityAttribute.setOptionsMap(metadataSelector.getAssociationAttributes(metaClassOfItem))
        associationEntityAttribute.setDatasource(importAttributeMapperDs, ENTITY_ATTRIBUTE_NAME)
        associationEntityAttribute.setNewOptionAllowed(true)
        associationEntityAttribute.caption = 'Entity Attribute'
        fieldConfig.setComponent(associationEntityAttribute)
    }

    def initAssociationLookupAttributeLookup() {
        FieldGroup.FieldConfig fieldConfig = fieldGroup.getField(ASSOCIATION_LOOKUP_NAME)
        associationLookupAttribute = componentsFactory.createComponent(LookupField)
        associationLookupAttribute.setDatasource(importAttributeMapperDs, ASSOCIATION_LOOKUP_NAME)

        if (item.attributeType == AttributeType.ASSOCIATION_ATTRIBUTE && item.entityAttribute) {
            activateAssociationLookupAttributes(item.entityAttribute)
        }
        else {
            associationLookupAttribute.enabled = false
        }
        fieldConfig.setComponent(associationLookupAttribute)



    }

    void activateAssociationLookupAttributes(String metaProperty) {

        if (metaProperty) {
            MetaClass importConfigurationMetaClass = metadata.session.getClass(item.configuration.entityClass)


            associationLookupAttribute.optionsMap = metadataSelector.getLookupMetaProperties(importConfigurationMetaClass.getProperty(metaProperty).domain.properties)

            associationLookupAttribute.enabled = true
        }
    }

    def initDynamicEntityAttributeLookup() {
        FieldGroup.FieldConfig fieldConfig = fieldGroup.getField(DYNAMIC_ENTITY_ATTRIBUTE_NAME)
        dynamicEntityAttribute = componentsFactory.createComponent(LookupField)
        dynamicEntityAttribute.setOptionsMap(metadataSelector.getDynamicAttributesLookupFieldOptions(metaClassOfItem))
        dynamicEntityAttribute.setDatasource(importAttributeMapperDs, DYNAMIC_ENTITY_ATTRIBUTE_NAME)
        fieldConfig.setComponent(dynamicEntityAttribute)
    }

    private MetaClass getMetaClassOfItem() {
        metadata.session.getClass(item.configuration.entityClass)
    }

    def initEntityAttributeLookup() {
        FieldGroup.FieldConfig fieldConfig = fieldGroup.getField(ENTITY_ATTRIBUTE_NAME)
        entityAttribute = componentsFactory.createComponent(LookupField)

        entityAttribute.setOptionsMap(metadataSelector.getDirectAttributesLookupFieldOptions(metaClassOfItem))
        entityAttribute.setDatasource(importAttributeMapperDs, ENTITY_ATTRIBUTE_NAME)
        fieldConfig.setComponent(entityAttribute)
    }

    void initAttributeTypeOptionsGroup() {

        FieldGroup.FieldConfig fieldConfig = fieldGroup.getField(ATTRIBUTE_TYPE_NAME)
        OptionsGroup component = componentsFactory.createComponent(OptionsGroup)
        component.setOptionsEnum(AttributeType)
        component.setDatasource(importAttributeMapperDs, ATTRIBUTE_TYPE_NAME)
        fieldConfig.setComponent(component)

        importAttributeMapperDs.addItemPropertyChangeListener(new Datasource.ItemPropertyChangeListener<ImportAttributeMapper>() {
            @Override
            void itemPropertyChanged(Datasource.ItemPropertyChangeEvent<ImportAttributeMapper> e) {
                if (e.property == ATTRIBUTE_TYPE_NAME) {
                    showLookupFieldsForAttributeType(e.value as AttributeType)
                }
            }
        })
    }

    void showLookupFieldsForAttributeType(AttributeType attributeType) {
        if (attributeType == AttributeType.DYNAMIC_ATTRIBUTE) {
            dynamicEntityAttribute.visible = true
            dynamicEntityAttribute.required = true
            entityAttribute.visible = false
            associationEntityAttribute.visible = false
            associationLookupAttribute.visible = false
        } else if (attributeType == AttributeType.DIRECT_ATTRIBUTE) {
            dynamicEntityAttribute.visible = false
            entityAttribute.visible = true
            associationEntityAttribute.visible = false
            associationLookupAttribute.visible = false
        } else if (attributeType == AttributeType.ASSOCIATION_ATTRIBUTE) {
            dynamicEntityAttribute.visible = false
            entityAttribute.visible = false
            associationEntityAttribute.visible = true
            associationLookupAttribute.visible = true
            associationLookupAttribute.required = true
        }
        else {
            dynamicEntityAttribute.visible = false
            entityAttribute.visible = false
            associationEntityAttribute.visible = false
            associationLookupAttribute.visible = false
        }
    }


}