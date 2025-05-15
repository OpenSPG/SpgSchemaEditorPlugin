package org.openspg.idea.conceptRule.structureView.viewElement;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.lang.psi.ConceptRuleCreateAction;
import org.openspg.idea.lang.psi.ConceptRuleTheDefineStructure;
import org.openspg.idea.lang.psi.ConceptRuleTheGraphStructure;
import org.openspg.idea.lang.psi.ConceptRuleTheRule;
import org.openspg.idea.schema.SchemaIcons;

import java.util.ArrayList;
import java.util.List;

public class ConceptRuleTheDefineStructureStructureViewElement extends AbstractConceptRuleStructureViewElement<ConceptRuleTheDefineStructure> {

    public ConceptRuleTheDefineStructureStructureViewElement(ConceptRuleTheDefineStructure element) {
        super(element);
    }

    @Override
    public String getNullableAlphaSortKey() {
        return myElement.getPredicatedDefine().getText();
    }

    @Override
    protected PresentationData createPresentation(ConceptRuleTheDefineStructure element) {
        return new PresentationData(
                element.getPredicatedDefine().getMajorLabel(),
                element.getPredicatedDefine().getMinorLabel(),
                SchemaIcons.Nodes.Entity,
                null
        );
    }

    @Override
    public TreeElement @NotNull [] getChildren() {
        List<PsiElement> elements = PsiTreeUtil.getChildrenOfAnyType(
                myElement.getBaseRuleDefine(),
                ConceptRuleTheGraphStructure.class,
                ConceptRuleTheRule.class,
                ConceptRuleCreateAction.class
        );

        List<TreeElement> treeElements = new ArrayList<>(elements.size());

        for (PsiElement element : elements) {
            if (element instanceof ConceptRuleTheGraphStructure theGraphElement) {
                treeElements.add(new ConceptRuleTheGraphStructureViewElement(theGraphElement));

            } else if (element instanceof ConceptRuleTheRule theRuleElement) {
                treeElements.add(new ConceptRuleTheRuleStructureViewElement(theRuleElement));

            } else if (element instanceof ConceptRuleCreateAction createActionElement) {
                treeElements.add(new ConceptRuleCreateActionStructureViewElement(createActionElement));

            } else {
                throw new IllegalArgumentException("Unknown element type: " + element.getClass().getName());
            }
        }

        return treeElements.toArray(new TreeElement[0]);
    }

}
