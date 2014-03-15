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
        // 读取样本集
        Map<Object, List<Sample>> samples = readSamples(attrNames);

        // 生成决策树
        Object decisionTree = generateDecisionTree(samples, attrNames);

        // 输出决策树
        outputDecisionTree(decisionTree, 0, null);
    }

    /**
     * 读取已分类的样本集，返回Map：分类 -> 属于该分类的样本的列表
     */
    static Map<Object, List<Sample>> readSamples(String[] attrNames) {

        // 样本属性及其所属分类（数组中的最后一个元素为样本所属分类）
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

        // 读取样本属性及其所属分类，构造表示样本的Sample对象，并按分类划分样本集
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
     * 构造决策树
     */
    @SuppressWarnings("unchecked")
    static Object generateDecisionTree(Map<Object, List<Sample>> categoryToSamples,
                                       String[] attrNames) {

        // 如果只有一个样本，将该样本所属分类作为---新样本的分类
        if (categoryToSamples.size() == 1) {
            return categoryToSamples.keySet().iterator().next();
        }

        // 如果没有供决策的属性，则将样本集中具有最多样本的分类作为新样本的分类
        // 即投票选举出分类
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

        // 选取测试属性
        Object[] rst = chooseBestTestAttribute(categoryToSamples, attrNames);

        Integer selectedAttrIndex = (Integer) rst[0];
        // Double minExpectEntropy = (Double) rst[1];
        Map<Object, Map<Object, List<Sample>>> spliteds = (Map<Object, Map<Object, List<Sample>>>) rst[2]; // 最优分支方案
        // 决策树根结点，分支属性为选取的测试属性
        Tree tree = new Tree(attrNames[selectedAttrIndex]);

        // 排除已选择的属性
        String[] subA = excludeSelectedAttribute(attrNames, selectedAttrIndex);

        // 分别对每一个分组进行递归调用决策树生成方法
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
     * 选取最优测试属性。 最优是指,如果根据选取的测试属性分支，则从各分支确定新样本
     * 的分类需要的信息量之和最小，这等价于确定新样本的测试属性获得的信息增益最大
     * 返回数组：选取的属性下标、信息量之和、Map(属性值->(分类->样本列表))
     */
    /**
     * @param categoryToSamples 待分类样本集合 Map<分类值（正或者负，或者其他分类）, List<Sample>>
     * @param attrNames 样本属性数组
     * @return
     */
    static Object[] chooseBestTestAttribute(Map<Object, List<Sample>> categoryToSamples,
                                            String[] attrNames) {

        int minIndex = -1; // 最优属性下标
        double minValue = Double.MAX_VALUE; // 最小信息量
        Map<Object, Map<Object, List<Sample>>> minSplits = null; // 最优分支方案

        // 对每一个属性，计算在该属性分类下的 信息增益
        for (int attrIndex = 0; attrIndex < attrNames.length; attrIndex++) {

            int allCount = 0; // 统计样本总数的计数器

            // curSplits 存储按照当前属性划分后的样本
            Map<Object, Map<Object, List<Sample>>> curSplits =
            /* NEW LINE */new HashMap<Object, Map<Object, List<Sample>>>();

            // 所有待划分的样本集合
            Set<Entry<Object, List<Sample>>> samplsSet = categoryToSamples
                    .entrySet();

            /**
             * 将样本按照当前属性划分成不同的分组
             */
            allCount += SplitSampleByAttribute(attrNames[attrIndex], curSplits,
                    samplsSet);

            // 计算将当前属性作为测试属性的情况下在各分支确定新样本的分类需要的信息量之和
            double expectGainInfo = getExpectEntropy(allCount, curSplits);

            // 选取最小为最优，期望熵最小，那么信息增益最大
            if (minValue > expectGainInfo) {
                minIndex = attrIndex;
                minValue = expectGainInfo;
                minSplits = curSplits;
            }
        }

        return new Object[] { minIndex, minValue, minSplits };
    }

    /**
     * 计算期望熵
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
                perSplitCount += list.size(); // 累计当前分支样本数

            double perSplitValue = 0.0; // 计数器：当前分支
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
        // 对每一个样本集合中的元素
        for (Entry<Object, List<Sample>> entry : samplsSet) {

            // 分类值(取值为正例或者反例)
            Object category = entry.getKey();
            // 分类值category对应的样本
            List<Sample> samples = entry.getValue();

            for (Sample sample : samples) {

                // 取样本的属性当前属性
                Object attrValue = sample.getAttribute(attrNames);

                // 在存储待分组样本的空间中查找是否有当前待选择的属性
                Map<Object, List<Sample>> split = curSplits.get(attrValue);
                if (split == null) {
                    // 第一次，这个划分为空，构造一个新存储空间，映射当前属性值和相应的存储空间
                    split = new HashMap<Object, List<Sample>>();
                    curSplits.put(attrValue, split);
                }

                // 从给定属性下的所有样本中，选择具有样本分类值为category的样本
                List<Sample> splitSamples = split.get(category);
                if (splitSamples == null) {
                    // 第一次为空，构建一个新的空间，存储当前属性取值下的样本
                    splitSamples = new LinkedList<Sample>();
                    split.put(category, splitSamples);
                }

                // 将当前样本添加到给定属性下的划分中
                splitSamples.add(sample);
            }

            allCount += samples.size();
        }
        return allCount;
    }

    /**
     * 将决策树输出到标准输出
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
     * 样本，包含多个属性和一个指明样本所属分类的分类值
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
     * 决策树（非叶结点），决策树中的每个非叶结点都引导了一棵决策树
     * 每个非叶结点包含一个分支属性和多个分支，分支属性的每个值对应一个分支，该分支引导了一棵子决策树
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
