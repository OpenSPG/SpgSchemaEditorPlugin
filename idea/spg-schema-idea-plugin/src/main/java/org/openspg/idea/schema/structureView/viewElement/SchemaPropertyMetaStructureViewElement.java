package org.openspg.idea.schema.structureView.viewElement;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.schema.SchemaIcons;
import org.openspg.idea.schema.lang.psi.SchemaPropertyMeta;
import org.openspg.idea.schema.lang.psi.SchemaSubProperty;

import java.util.ArrayList;
import java.util.List;

public class SchemaPropertyMetaStructureViewElement extends AbstractSchemaStructureViewElement<SchemaPropertyMeta> {

    public SchemaPropertyMetaStructureViewElement(SchemaPropertyMeta element) {
        super(element);
    }

    @Override
    public String getNullableAlphaSortKey() {
        return myElement.getPropertyMetaHead().getBasicPropertyDeclaration().getName();
    }

    @Override
    protected PresentationData createPresentation(SchemaPropertyMeta element) {
        return new PresentationData(
                element.getPropertyMetaHead().getBasicPropertyDeclaration().getName(),
                element.getPropertyMetaHead().getBasicPropertyDeclaration().getValue(),
                element.getPropertyMetaBody().getSubPropertyList().isEmpty() ? SchemaIcons.Nodes.EmptyMeta : SchemaIcons.Nodes.PropertyMeta,
                null
        );
    }

    @Override
    public TreeElement @NotNull [] getChildren() {
        List<SchemaSubProperty> elements = PsiTreeUtil.getChildrenOfTypeAsList(myElement, SchemaSubProperty.class);

        List<TreeElement> treeElements = new ArrayList<>(elements.size());
        for (SchemaSubProperty element : elements) {
            treeElements.add(new SchemaSubPropertyStructureViewElement(element));
        }
        return treeElements.toArray(new TreeElement[0]);
    }

}
