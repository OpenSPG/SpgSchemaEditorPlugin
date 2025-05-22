package org.openspg.idea.schema.structureView.viewElement;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.schema.SchemaIcons;
import org.openspg.idea.schema.lang.psi.SchemaSubPropertyMeta;

public class SchemaSubPropertyMetaStructureViewElement extends AbstractSchemaStructureViewElement<SchemaSubPropertyMeta> {

    public SchemaSubPropertyMetaStructureViewElement(SchemaSubPropertyMeta element) {
        super(element);
    }

    @Override
    public String getNullableAlphaSortKey() {
        return myElement.getBasicPropertyDeclaration().getName();
    }

    @Override
    protected PresentationData createPresentation(SchemaSubPropertyMeta element) {
        return new PresentationData(
                element.getBasicPropertyDeclaration().getName(),
                element.getBasicPropertyDeclaration().getValue(),
                SchemaIcons.Nodes.SubPropertyMeta,
                null);
    }

    @Override
    public TreeElement @NotNull [] getChildren() {
        return TreeElement.EMPTY_ARRAY;
    }

}
