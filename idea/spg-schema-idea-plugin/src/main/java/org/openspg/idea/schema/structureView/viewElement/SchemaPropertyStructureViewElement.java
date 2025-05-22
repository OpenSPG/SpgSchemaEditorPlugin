package org.openspg.idea.schema.structureView.viewElement;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.schema.SchemaIcons;
import org.openspg.idea.schema.lang.psi.SchemaProperty;
import org.openspg.idea.schema.lang.psi.SchemaPropertyMeta;

import java.util.ArrayList;
import java.util.List;

public class SchemaPropertyStructureViewElement extends AbstractSchemaStructureViewElement<SchemaProperty> {

    public SchemaPropertyStructureViewElement(SchemaProperty element) {
        super(element);
    }

    @Override
    public String getNullableAlphaSortKey() {
        return myElement.getPropertyHead().getBasicStructureDeclaration().getStructureNameDeclaration().getText();
    }

    @Override
    protected PresentationData createPresentation(SchemaProperty element) {
        return new PresentationData(
                element.getPropertyHead().getBasicStructureDeclaration().getStructureNameDeclaration().getText(),
                element.getPropertyHead().getBasicStructureDeclaration().getStructureAliasDeclaration().getText(),
                SchemaIcons.Nodes.Property,
                null
        );
    }

    @Override
    public TreeElement @NotNull [] getChildren() {
        List<SchemaPropertyMeta> elements = PsiTreeUtil.getChildrenOfTypeAsList(myElement, SchemaPropertyMeta.class);

        List<TreeElement> treeElements = new ArrayList<>(elements.size());
        for (SchemaPropertyMeta element : elements) {
            treeElements.add(new SchemaPropertyMetaStructureViewElement(element));
        }
        return treeElements.toArray(new TreeElement[0]);
    }

}
