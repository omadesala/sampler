package com.sample.ui;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;

import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Material;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class Sample3D extends Applet {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public BranchGroup createSceneGraph() {

        BranchGroup objRoot = new BranchGroup(); // objRoot�������������Ĺ��췽���ṩ����createSceneGraph�����������
        TransformGroup objTrans = new TransformGroup(); // �½�objTrans�������ڴ������ϵ
        objRoot.addChild(objTrans); // ȱʡ״����Java3d�Ĺ۲��λ��(0,0,2.41)����������objTrans���䲻ҪҲ�����гɹ�

        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
            100.0); // �½��������ƿ�������ԭ��Ϊ���ģ��뾶100

        Color3f bgColor = new Color3f(1.0f, 1.0f, 1.0f); // ���ñ���ɫΪ��ɫ
        Background bg = new Background(bgColor); // ���ñ�������bg����ɫΪbgColor
        bg.setApplicationBounds(bounds); // ���ñ���Ӧ�÷�ΧΪbounds
        objRoot.addChild(bg); // ����������bg��ӵ�objRoot������

        Color3f directionalColor = new Color3f(1.f, 0.f, 0.f);// ���ù�ԴΪ��ɫ
        Vector3f vec = new Vector3f(-1.f, 0.f, -1.f); // ���ù�ԴΪ-Z����
        DirectionalLight directionalLight = new DirectionalLight(
            directionalColor, vec);// ʹ��ֱ�ӹ���
        directionalLight.setInfluencingBounds(bounds); // ���ù���Ӱ�췶ΧΪbounds
        objRoot.addChild(directionalLight); // �����ն�����ӵ�objRoot��

        Appearance app = new Appearance();
        Material material = new Material();
        // material.setEmissiveColor(new Color3f(1.0f,0.0f,0.0f));//��� �޹���������
        // ����Ϊ������ɫ������Ϊ��ɫ
        material.setDiffuseColor(new Color3f(1.0f, 1.0f, 0.0f)); // �й��������£�����Ϊ��������ɫ������Ϊ��ɫ
        app.setMaterial(material); // ���ò���material������app��materialΪ�ջ�ʹ����ʧЧ

        Cone cone = new Cone(.5f, 1.0f, 1, app);// ���ư뾶Ϊ0.5,��Ϊ1.0��Բ׶��ƬԪ���Ϊ1�����Ϊapp��������ΪԲ׶�������˲������ԣ�
        objTrans.addChild(cone);

        objRoot.compile();
        return objRoot;
    }

    public Sample3D() {
        setLayout(new BorderLayout());
        // Canvas3D c=new Canvas3D(null);
        GraphicsConfiguration config = SimpleUniverse
            .getPreferredConfiguration();
        Canvas3D c = new Canvas3D(config);
        // ����һ�任���������䣻�ǵõ���import java.awt.GraphicsConfiguration��
        add("Center", c);// applet�ķ��������ڽ�ָ�����������c����ӵ���������applet����
        BranchGroup scene = createSceneGraph();
        SimpleUniverse u = new SimpleUniverse(c); // ��3d����ʵ��c����һ���ֲ���һ�Ĳ鿴ƽ̨��һ���۲��߶���
        u.getViewingPlatform().setNominalViewingTransform();
        u.addBranchGraph(scene);

    }
    // public static void main(String args[])
    // {
    // new MainFrame(new SimpleCone(),400,300);
    // }

}