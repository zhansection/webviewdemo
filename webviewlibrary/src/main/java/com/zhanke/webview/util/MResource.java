package com.zhanke.webview.util;

import android.content.Context;

/**
 * 根据资源的名字获取其ID值
 */
public class MResource {
	public final static String id = "id";
	public final static String layout = "layout";
	public final static String drawable = "drawable";
	public final static String mipmap = "mipmap";
	public final static String anim = "anim";
	public final static String color = "color";
	public final static String string = "string";
	public final static String style = "style";
	public final static String styleable = "styleable";
	private static int getIdByName(Context context, String className, String name) {
		String packageName = context.getPackageName();
		Class r;
		int id = 0;
		try {
			r = Class.forName(packageName + ".R");
			Class[] classes = r.getClasses();
			Class desireClass = null;
			for (int i = 0; i < classes.length; ++i) {
				if (classes[i].getName().split("\\$")[1].equals(className)) {
					desireClass = classes[i];
					break;
				}
			}
			if (desireClass != null){
				id = desireClass.getField(name).getInt(desireClass);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		return id;
	}

	private static int[] getIdsByName(Context context, String className,
			String name) {
		String packageName = context.getPackageName();
		Class r;
		int[] ids = null;
		try {
			r = Class.forName(packageName + ".R");

			Class[] classes = r.getClasses();
			Class desireClass = null;

			for (int i = 0; i < classes.length; ++i) {
				if (classes[i].getName().split("\\$")[1].equals(className)) {
					desireClass = classes[i];
					break;
				}
			}

			if ((desireClass != null)
					&& (desireClass.getField(name).get(desireClass) != null)
					&& (desireClass.getField(name).get(desireClass).getClass()
							.isArray()))
				ids = (int[]) desireClass.getField(name).get(desireClass);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		return ids;
	}

	public static int getID(Context context, String name) {
		return getIdByName(context, id, name);
	}

	public static int getLayout(Context context, String name) {
		return getIdByName(context, layout, name);
	}

	public static int getDrawable(Context context, String name) {
		return getIdByName(context, drawable, name);
	}

	public static int getMipmap(Context context, String name) {
		return getIdByName(context, mipmap, name);
	}

	public static int getAnim(Context context, String name) {
		return getIdByName(context, anim, name);
	}

	public static int getColor(Context context, String name) {
		return getIdByName(context, color, name);
	}

	public static int getString(Context context, String name) {
		return getIdByName(context, string, name);
	}

	public static int getStyle(Context context, String name) {
		return getIdByName(context, style, name);
	}

	public static int getStyleable(Context context, String name) {
		return getIdByName(context, styleable, name);
	}

	public static int[] getStyleables(Context context, String name) {
		return getIdsByName(context, styleable, name);
	}
}