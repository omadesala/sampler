package com.sample.classify;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class DicisionTree {

    public static void main(String[] args) throws Exception {
        String[] attrNames = new String[] { "AGE", "INCOME", "STUDENT",
                "CREDIT_RATING" };
        // ��ȡ������
        Map<Object, List<Sample>> samples = readSamples(attrNames);

        // ���ɾ�����
        Object decisionTree = generateDecisionTree(samples, attrNames);

        // ���������
        outputDecisionTree(decisionTree, 0, null);
    }

    /**
     * ��ȡ�ѷ����������������Map������ -> ���ڸ÷�����������б�
     */
    static Map<Object, List<Sample>> readSamples(String[] attrNames) {

        // �������Լ����������ࣨ�����е����һ��Ԫ��Ϊ�����������ࣩ
        Object[][] rawData = new Object[][] {
                { "<30  ", "High  ", "No ", "Fair     ", "0" },
                { "<30  ", "High  ", "No ", "Excellent", "0" },
                { "30-40", "High  ", "No ", "Fair     ", "1" },
                { ">40  ", "Medium", "No ", "Fair     ", "1" },
                { ">40  ", "Low   ", "Yes", "Fair     ", "1" },
                { ">40  ", "Low   ", "Yes", "Excellent", "0" },
                { "30-40", "Low   ", "Yes", "Excellent", "1" },
                { "<30  ", "Medium", "No ", "Fair     ", "0" },
                { "<30  ", "Low   ", "Yes", "Fair     ", "1" },
                { ">40  ", "Medium", "Yes", "Fair     ", "1" },
                { "<30  ", "Medium", "Yes", "Excellent", "1" },
                { "30-40", "Medium", "No ", "Excellent", "1" },
                { "30-40", "High  ", "Yes", "Fair     ", "1" },
                { ">40  ", "Medium", "No ", "Excellent", "0" } };

        // ��ȡ�������Լ����������࣬�����ʾ������Sample���󣬲������໮��������
        Map<Object, List<Sample>> ret = new HashMap<Object, List<Sample>>();
        for (Object[] row : rawData) {
            Sample sample = new Sample();
            int i = 0;
            for (int n = row.length - 1; i < n; i++)
                sample.setAttribute(attrNames[i], row[i]);
            sample.setCategory(row[i]);
            List<Sample> samples = ret.get(row[i]);
            if (samples == null) {
                samples = new LinkedList<Sample>();
                ret.put(row[i], samples);
            }
            samples.add(sample);
        }

        return ret;
    }

    /**
     * ���������
     */
    @SuppressWarnings("unchecked")
    static Object generateDecisionTree(Map<Object, List<Sample>> categoryToSamples,
                                       String[] attrNames) {

        // ���ֻ��һ��������������������������Ϊ---�������ķ���
        if (categoryToSamples.size() == 1) {
            return categoryToSamples.keySet().iterator().next();
        }

        // ���û�й����ߵ����ԣ����������о�����������ķ�����Ϊ�������ķ���
        // ��ͶƱѡ�ٳ�����
        if (attrNames.length == 0) {
            int max = 0;
            Object maxCategory = null;
            for (Entry<Object, List<Sample>> entry : categoryToSamples
                    .entrySet()) {
                int cur = entry.getValue().size();
                if (cur > max) {
                    max = cur;
                    maxCategory = entry.getKey();
                }
            }
            return maxCategory;
        }

        // ѡȡ��������
        Object[] rst = chooseBestTestAttribute(categoryToSamples, attrNames);

        Integer selectedAttrIndex = (Integer) rst[0];
        // Double minExpectEntropy = (Double) rst[1];
        Map<Object, Map<Object, List<Sample>>> spliteds = (Map<Object, Map<Object, List<Sample>>>) rst[2]; // ���ŷ�֧����
        // ����������㣬��֧����Ϊѡȡ�Ĳ�������
        Tree tree = new Tree(attrNames[selectedAttrIndex]);

        // �ų���ѡ�������
        String[] subA = excludeSelectedAttribute(attrNames, selectedAttrIndex);

        // �ֱ��ÿһ��������еݹ���þ��������ɷ���
        handleSplitedSample(tree, subA, spliteds);

        return tree;
    }

    private static void handleSplitedSample(Tree tree,
                                            String[] subA,
                                            Map<Object, Map<Object, List<Sample>>> splits) {
        for (Entry<Object, Map<Object, List<Sample>>> entry : splits.entrySet()) {

            Object attrValue = entry.getKey();
            Map<Object, List<Sample>> split = entry.getValue();

            Object child = generateDecisionTree(split, subA);
            tree.setChild(attrValue, child);
        }
    }

    private static String[] excludeSelectedAttribute(String[] attrNames,
                                                     Integer selectedAttrIndex) {
        String[] subA = new String[attrNames.length - 1];
        for (int i = 0, j = 0; i < attrNames.length; i++)
            if (i != selectedAttrIndex) {
                subA[j++] = attrNames[i];
            }
        return subA;
    }

    /**
     * ѡȡ���Ų������ԡ� ������ָ,�������ѡȡ�Ĳ������Է�֧����Ӹ���֧ȷ��������
     * �ķ�����Ҫ����Ϣ��֮����С����ȼ���ȷ���������Ĳ������Ի�õ���Ϣ�������
     * �������飺ѡȡ�������±ꡢ��Ϣ��֮�͡�Map(����ֵ->(����->�����б�))
     */
    /**
     * @param categoryToSamples �������������� Map<����ֵ�������߸��������������ࣩ, List<Sample>>
     * @param attrNames ������������
     * @return
     */
    static Object[] chooseBestTestAttribute(Map<Object, List<Sample>> categoryToSamples,
                                            String[] attrNames) {

        int minIndex = -1; // ���������±�
        double minValue = Double.MAX_VALUE; // ��С��Ϣ��
        Map<Object, Map<Object, List<Sample>>> minSplits = null; // ���ŷ�֧����

        // ��ÿһ�����ԣ������ڸ����Է����µ� ��Ϣ����
        for (int attrIndex = 0; attrIndex < attrNames.length; attrIndex++) {

            int allCount = 0; // ͳ�����������ļ�����

            // curSplits �洢���յ�ǰ���Ի��ֺ������
            Map<Object, Map<Object, List<Sample>>> curSplits =
            /* NEW LINE */new HashMap<Object, Map<Object, List<Sample>>>();

            // ���д����ֵ���������
            Set<Entry<Object, List<Sample>>> samplsSet = categoryToSamples
                    .entrySet();

            /**
             * ���������յ�ǰ���Ի��ֳɲ�ͬ�ķ���
             */
            allCount += SplitSampleByAttribute(attrNames[attrIndex], curSplits,
                    samplsSet);

            // ���㽫��ǰ������Ϊ�������Ե�������ڸ���֧ȷ���������ķ�����Ҫ����Ϣ��֮��
            double expectGainInfo = getExpectEntropy(allCount, curSplits);

            // ѡȡ��СΪ���ţ���������С����ô��Ϣ�������
            if (minValue > expectGainInfo) {
                minIndex = attrIndex;
                minValue = expectGainInfo;
                minSplits = curSplits;
            }
        }

        return new Object[] { minIndex, minValue, minSplits };
    }

    /**
     * ����������
     * @param allCount
     * @param curSplits
     * @return
     */
    private static double getExpectEntropy(int allCount,
                                           Map<Object, Map<Object, List<Sample>>> curSplits) {
        double curValue = 0;
        for (Map<Object, List<Sample>> splits : curSplits.values()) {

            double perSplitCount = 0;
            for (List<Sample> list : splits.values())
                perSplitCount += list.size(); // �ۼƵ�ǰ��֧������

            double perSplitValue = 0.0; // ����������ǰ��֧
            for (List<Sample> list : splits.values()) {

                double p = list.size() / perSplitCount;
                perSplitValue -= p * (Math.log(p) / Math.log(2));
            }

            curValue += (perSplitCount / allCount) * perSplitValue;
        }
        return curValue;
    }

    private static int SplitSampleByAttribute(String attrNames,
                                              Map<Object, Map<Object, List<Sample>>> curSplits,
                                              Set<Entry<Object, List<Sample>>> samplsSet) {

        int allCount = 0;
        // ��ÿһ�����������е�Ԫ��
        for (Entry<Object, List<Sample>> entry : samplsSet) {

            // ����ֵ(ȡֵΪ�������߷���)
            Object category = entry.getKey();
            // ����ֵcategory��Ӧ������
            List<Sample> samples = entry.getValue();

            for (Sample sample : samples) {

                // ȡ���������Ե�ǰ����
                Object attrValue = sample.getAttribute(attrNames);

                // �ڴ洢�����������Ŀռ��в����Ƿ��е�ǰ��ѡ�������
                Map<Object, List<Sample>> split = curSplits.get(attrValue);
                if (split == null) {
                    // ��һ�Σ��������Ϊ�գ�����һ���´洢�ռ䣬ӳ�䵱ǰ����ֵ����Ӧ�Ĵ洢�ռ�
                    split = new HashMap<Object, List<Sample>>();
                    curSplits.put(attrValue, split);
                }

                // �Ӹ��������µ����������У�ѡ�������������ֵΪcategory������
                List<Sample> splitSamples = split.get(category);
                if (splitSamples == null) {
                    // ��һ��Ϊ�գ�����һ���µĿռ䣬�洢��ǰ����ȡֵ�µ�����
                    splitSamples = new LinkedList<Sample>();
                    split.put(category, splitSamples);
                }

                // ����ǰ������ӵ����������µĻ�����
                splitSamples.add(sample);
            }

            allCount += samples.size();
        }
        return allCount;
    }

    /**
     * ���������������׼���
     */
    static void outputDecisionTree(Object obj, int level, Object from) {
        for (int i = 0; i < level; i++)
            System.out.print("|-----");
        if (from != null)
            System.out.printf("(%s):", from);
        if (obj instanceof Tree) {
            Tree tree = (Tree) obj;
            String attrName = tree.getAttribute();
            System.out.printf("[%s = ?]\n", attrName);
            for (Object attrValue : tree.getAttributeValues()) {
                Object child = tree.getChild(attrValue);
                outputDecisionTree(child, level + 1, attrName + " = "
                        + attrValue);
            }
        } else {
            System.out.printf("[CATEGORY = %s]\n", obj);
        }
    }

    /**
     * ����������������Ժ�һ��ָ��������������ķ���ֵ
     */
    static class Sample {

        private Map<String, Object> attributes = new HashMap<String, Object>();

        private Object category;

        public Object getAttribute(String name) {
            return attributes.get(name);
        }

        public void setAttribute(String name, Object value) {
            attributes.put(name, value);
        }

        public Object getCategory() {
            return category;
        }

        public void setCategory(Object category) {
            this.category = category;
        }

        public String toString() {
            return attributes.toString();
        }

    }

    /**
     * ����������Ҷ��㣩���������е�ÿ����Ҷ��㶼������һ�þ�����
     * ÿ����Ҷ������һ����֧���ԺͶ����֧����֧���Ե�ÿ��ֵ��Ӧһ����֧���÷�֧������һ���Ӿ�����
     */
    static class Tree {

        private String attribute;

        private Map<Object, Object> children = new HashMap<Object, Object>();

        public Tree(String attribute) {
            this.attribute = attribute;
        }

        public String getAttribute() {
            return attribute;
        }

        public Object getChild(Object attrValue) {
            return children.get(attrValue);
        }

        public void setChild(Object attrValue, Object child) {
            children.put(attrValue, child);
        }

        public Set<Object> getAttributeValues() {
            return children.keySet();
        }

    }

}
