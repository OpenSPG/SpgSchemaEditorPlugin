package org.openspg.idea.schema.structureView.viewElement;

import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.schema.lang.psi.SchemaEntity;

import java.util.ArrayList;
import java.util.List;

public class SchemaFileStructureViewElement extends AbstractSchemaStructureViewElement<PsiFile> {

    public SchemaFileStructureViewElement(PsiFile element) {
        super(element);
    }

    @NotNull
    @Override
    public String getAlphaSortKey() {
        return myElement.getName();
    }

    @Override
    public TreeElement @NotNull [] getChildren() {
        List<SchemaEntity> elements = PsiTreeUtil.getChildrenOfTypeAsList(myElement, SchemaEntity.class);

        List<TreeElement> treeElements = new ArrayList<>(elements.size());

        for (SchemaEntity element : elements) {
            treeElements.add(new SchemaEntityStructureViewElement(element));
        }

        return treeElements.toArray(new TreeElement[0]);
    }

}
