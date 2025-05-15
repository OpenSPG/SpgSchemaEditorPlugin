package org.openspg.idea.conceptRule.structureView.viewElement;

import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.lang.psi.ConceptRuleRuleWrapper;

import java.util.ArrayList;
import java.util.List;

public class ConceptRuleFileStructureViewElement extends AbstractConceptRuleStructureViewElement<PsiFile> {

    public ConceptRuleFileStructureViewElement(PsiFile element) {
        super(element);
    }

    @NotNull
    @Override
    public String getAlphaSortKey() {
        return myElement.getName();
    }

    @Override
    public TreeElement @NotNull [] getChildren() {
        List<ConceptRuleRuleWrapper> elements = PsiTreeUtil.getChildrenOfTypeAsList(myElement, ConceptRuleRuleWrapper.class);

        List<TreeElement> treeElements = new ArrayList<>(elements.size());

        for (ConceptRuleRuleWrapper element : elements) {
            treeElements.add(new ConceptRuleRuleWrapperStructureViewElement(element));
        }

        return treeElements.toArray(new TreeElement[0]);
    }

}
