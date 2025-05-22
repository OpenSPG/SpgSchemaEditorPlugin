package org.openspg.idea.schema.structureView.viewElement;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.schema.SchemaIcons;
import org.openspg.idea.schema.lang.psi.SchemaSubProperty;
import org.openspg.idea.schema.lang.psi.SchemaSubPropertyMeta;

import java.util.ArrayList;
import java.util.List;

public class SchemaSubPropertyStructureViewElement extends AbstractSchemaStructureViewElement<SchemaSubProperty> {

    public SchemaSubPropertyStructureViewElement(SchemaSubProperty element) {
        super(element);
    }

    @Override
    public String getNullableAlphaSortKey() {
        return myElement.getSubPropertyHead().getBasicStructureDeclaration().getStructureNameDeclaration().getText();
    }

    @Override
    protected PresentationData createPresentation(SchemaSubProperty element) {
        return new PresentationData(
                myElement.getSubPropertyHead().getBasicStructureDeclaration().getStructureNameDeclaration().getText(),
                myElement.getSubPropertyHead().getBasicStructureDeclaration().getStructureAliasDeclaration().getText(),
                SchemaIcons.Nodes.SubProperty,
                null
        );
    }

    @Override
    public TreeElement @NotNull [] getChildren() {
        List<SchemaSubPropertyMeta> elements = PsiTreeUtil.getChildrenOfTypeAsList(myElement, SchemaSubPropertyMeta.class);

        List<TreeElement> treeElements = new ArrayList<>(elements.size());

        for (SchemaSubPropertyMeta element : elements) {
            treeElements.add(new SchemaSubPropertyMetaStructureViewElement(element));
        }

        return treeElements.toArray(new TreeElement[0]);
    }

}
