package org.openspg.idea.conceptRule.structureView.viewElement;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.lang.psi.ConceptRuleRuleWrapper;
import org.openspg.idea.lang.psi.ConceptRuleRuleWrapperBody;
import org.openspg.idea.lang.psi.ConceptRuleTheDefineStructure;
import org.openspg.idea.schema.SchemaIcons;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ConceptRuleRuleWrapperStructureViewElement extends AbstractConceptRuleStructureViewElement<ConceptRuleRuleWrapper> {

    public ConceptRuleRuleWrapperStructureViewElement(ConceptRuleRuleWrapper element) {
        super(element);
    }

    @Override
    public String getNullableAlphaSortKey() {
        return myElement.getLabel();
    }

    @Override
    protected PresentationData createPresentation(ConceptRuleRuleWrapper element) {
        List<String> labels = new ArrayList<>();
        List<String> locations = new ArrayList<>();

        Stream.of(element.getLabel().split(":"))
                .map(label -> Stream.of(label.split("/"))
                        .map(String::trim)
                        .map(x -> StringUtils.unwrap(x, "`"))
                        .map(x -> StringUtils.unwrap(x, "'"))
                        .map(x -> StringUtils.unwrap(x, "\""))
                        .toList())
                .forEach(texts -> {
                    if (!texts.isEmpty()) {
                        labels.add(texts.get(0));
                    }
                    if (texts.size() > 1) {
                        locations.add(texts.get(1));
                    }
                });

        return new PresentationData(
                String.join("/", labels),
                String.join("/", locations),
                SchemaIcons.Nodes.Entity,
                null
        );
    }

    @Override
    public TreeElement @NotNull [] getChildren() {
        ConceptRuleRuleWrapperBody ruleWrapperBodyElement = myElement.getRuleWrapperBody();
        if (ruleWrapperBodyElement == null) {
            return TreeElement.EMPTY_ARRAY;
        }

        List<ConceptRuleTheDefineStructure> elements = PsiTreeUtil.getChildrenOfTypeAsList(ruleWrapperBodyElement, ConceptRuleTheDefineStructure.class);

        List<TreeElement> treeElements = new ArrayList<>(elements.size());
        for (ConceptRuleTheDefineStructure element : elements) {
            treeElements.add(new ConceptRuleTheDefineStructureStructureViewElement(element));
        }

        return treeElements.toArray(new TreeElement[0]);
    }

}
