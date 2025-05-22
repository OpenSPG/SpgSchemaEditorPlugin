package org.openspg.idea.schema.structureView.viewElement;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.schema.SchemaIcons;
import org.openspg.idea.schema.lang.psi.SchemaEntity;
import org.openspg.idea.schema.lang.psi.SchemaEntityHead;
import org.openspg.idea.schema.lang.psi.SchemaEntityMeta;

import java.util.ArrayList;
import java.util.List;

public class SchemaEntityStructureViewElement extends AbstractSchemaStructureViewElement<SchemaEntity> {

    public SchemaEntityStructureViewElement(SchemaEntity element) {
        super(element);
    }

    @Override
    public String getNullableAlphaSortKey() {
        return myElement.getEntityHead().getBasicStructureDeclaration().getStructureNameDeclaration().getText();
    }

    @Override
    protected PresentationData createPresentation(SchemaEntity element) {
        SchemaEntityHead head = element.getEntityHead();
        return new PresentationData(
                head.getBasicStructureDeclaration().getStructureNameDeclaration().getText(),
                head.getBasicStructureDeclaration().getStructureAliasDeclaration().getText(),
                SchemaIcons.Nodes.Entity,
                null
        );
    }

    @Override
    public TreeElement @NotNull [] getChildren() {
        List<SchemaEntityMeta> elements = PsiTreeUtil.getChildrenOfTypeAsList(myElement, SchemaEntityMeta.class);

        List<TreeElement> treeElements = new ArrayList<>(elements.size());

        for (SchemaEntityMeta element : elements) {
            treeElements.add(new SchemaEntityMetaStructureViewElement(element));
        }

        return treeElements.toArray(new TreeElement[0]);
    }

}
