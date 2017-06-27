package com.cj.mobile.common.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import timber.log.Timber;

/**
 * 排序工具类
 *
 * @author 王力杨
 */
public class ListSort<E> {
    /**
     * * List对象排序的通用方法(针对汉字、英文相同值排序，例如：a、b、c、ab、bc；输出结果为：a、ab、b、bc、c)
     * <p/>
     * ArrayList<Student> list = new ArrayList<Student>();
     * <p/>
     * list.add(new Student(1,"张三",2));//id,name,age
     * <p/>
     * list.add(new Student(2,"李四",4));
     * <p/>
     * list.add(new Student(3,"王五",1));
     * <p/>
     * list.add(new Student(4,"小明",5));
     * <p/>
     * list.add(new Student(5,"小黑",3));
     * <p/>
     * ListSort<Student> listSort= new ListSort<Student>();
     * <p/>
     * listSort.Sort(list, "getAge", "desc");
     * <p/>
     * for(Student s:list){
     * <p/>
     * Timber.d(s.getId()+s.getName()+s.getAge());
     * <p/>
     *
     * @param list   要排序的集合
     * @param method 要排序的实体的属性所对应的get方法
     * @param sort   desc 为正序
     */
    @SuppressWarnings("all")
    public void SortByStr(List<E> list, final String method, final String sort) {
        // 用内部类实现排序
        Collections.sort(list, new Comparator<E>() {

            public int compare(E a, E b) {
                int ret = 0;
                try {
                    // 获取m1的方法名
                    Method m1 = a.getClass().getMethod(method, new Class[]{});
                    // 获取m2的方法名
                    Method m2 = b.getClass().getMethod(method, new Class[]{});

                    if (sort != null && "desc".equals(sort)) {

                        ret = m2.invoke(((E) b), new Object[]{}).toString()
                                .compareTo(m1.invoke(((E) a), new Object[]{}).toString());

                    } else {
                        // 正序排序
                        ret = m1.invoke(((E) a), new Object[]{}).toString()
                                .compareTo(m2.invoke(((E) b), new Object[]{}).toString());
                    }
                } catch (NoSuchMethodException ne) {
                    Timber.e(ne.getMessage());
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                return ret;
            }
        });
    }

    /**
     * * List对象排序的通用方法(针对数值排序进行大小)
     * <p/>
     * ArrayList<Student> list = new ArrayList<Student>();
     * <p/>
     * list.add(new Student(1,"张三",2));//id,name,age
     * <p/>
     * list.add(new Student(2,"李四",4));
     * <p/>
     * list.add(new Student(3,"王五",1));
     * <p/>
     * list.add(new Student(4,"小明",5));
     * <p/>
     * list.add(new Student(5,"小黑",3));
     * <p/>
     * ListSort<Student> listSort= new ListSort<Student>();
     * <p/>
     * listSort.Sort(list, "getAge", "desc");
     * <p/>
     * for(Student s:list){
     * <p/>
     * Timber.d(s.getId()+s.getName()+s.getAge());
     * <p/>
     *
     * @param list   要排序的集合
     * @param method 要排序的实体的属性所对应的get方法
     * @param sort   desc 为正序
     */
    public void SortByNum(List<E> list, final String method, final String sort) {
        // 用内部类实现排序
        Collections.sort(list, new Comparator<E>() {

            public int compare(E a, E b) {
                int ret = 0;
                try {
                    // 获取m1的方法名
                    Method m1 = a.getClass().getMethod(method, new Class<?>[]{});
                    // 获取m2的方法名
                    Method m2 = b.getClass().getMethod(method, new Class<?>[]{});

                    if (sort != null && "desc".equals(sort)) {

                        ret = String.valueOf(m2.invoke(((E) b), new Object[]{}).toString().length()).compareTo(String.valueOf(m1.invoke(((E) a), new Object[]{}).toString().length()));
                        if (m2.invoke(((E) b), new Object[]{}).toString().length() == m1.invoke(((E) a), new Object[]{}).toString().length()) {
                            ret = m2.invoke(((E) b), new Object[]{}).toString().compareTo(m1.invoke(((E) a), new Object[]{}).toString());
                        }

                    } else {
                        // 正序排序
                        Integer num1 = Integer.parseInt(m1.invoke(((E) a), new Object[]{}).toString());
                        Integer num2 = Integer.parseInt(m2.invoke(((E) b), new Object[]{}).toString());

//						ret = String.valueOf((m1.invoke(((E)a), null).toString().length())).compareTo(String.valueOf(m2.invoke(((E)b), null).toString().length()));
                        ret = num1.compareTo(num2);
                    }
                } catch (NoSuchMethodException ne) {
                    Timber.e(ne.getMessage());
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                return ret;
            }
        });
    }

    /**
     * 按键排序(sort by key)
     * <p/>
     * key的类型为String；内容为int
     * <p/>
     *
     * @param oriMap 需要排序的对象
     * @return 排序完成的对象
     */
    public Map<?, ?> sortMapByKeyTypeInt(Map<?, ?> oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        Map<Object, Object> sortedMap = new TreeMap<Object, Object>(
                new Comparator<Object>() {
                    public int compare(Object key1, Object key2) {
                        int intKey1 = 0, intKey2 = 0;
                        try {
                            intKey1 = getInt(key1.toString());
                            intKey2 = getInt(key2.toString());
                        } catch (Exception e) {
                            intKey1 = 0;
                            intKey2 = 0;
                        }
                        return intKey1 - intKey2;
                    }
                });
        sortedMap.putAll(oriMap);
        return sortedMap;
    }

    /**
     * 将字体串转换成int
     */
    private int getInt(String str) {
        int i = 0;
        try {
            Pattern p = Pattern.compile("^\\d+");
            Matcher m = p.matcher(str);
            if (m.find()) {
                i = Integer.valueOf(m.group());
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * 按值排序(sort by value)
     * <p>
     * Value的类型为String；内容为Int
     * <p>
     *
     * @param oriMap
     *            需要排序的对象
     * @return 排序完成的对象
     *//*
    @SuppressWarnings("unchecked")
	public Map<?, ?> sortMapByValueTypeInt(Map<?, ?> oriMap) {
		Map<Object, Object> sortedMap = new LinkedHashMap<Object, Object>();
		if (oriMap != null && !oriMap.isEmpty()) {
			List<Map.Entry<Object, Object>> entryList = new ArrayList<Map.Entry<Object, Object>>((Collection<? extends Entry<Object, Object>>) oriMap.entrySet());
			Collections.sort(entryList,
					new Comparator<Map.Entry<Object, Object>>() {
						public int compare(Entry<Object, Object> entry1,
								Entry<Object, Object> entry2) {
							int value1 = 0, value2 = 0;
							try {
								value1 = getInt(entry1.getValue().toString());
								value2 = getInt(entry2.getValue().toString());
							} catch (NumberFormatException e) {
								value1 = 0;
								value2 = 0;
							}
							return value2 - value1;
						}
					});
			Iterator<Map.Entry<Object, Object>> iter = entryList.iterator();
			Map.Entry<Object, Object> tmpEntry = null;
			while (iter.hasNext()) {
				tmpEntry = iter.next();
				sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
			}
		}
		return sortedMap;
	}*/

    /**
     * 使用 Map按key进行排序
     *
     * @param map
     * @param desc 排序方式(desc为降序，asc升序)，默认降序
     * @return
     */
    public static Map<Object, Object> sortMapByKey(Map<Object, Object> map, final String desc) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<Object, Object> sortMap = new TreeMap<Object, Object>(
                new Comparator<Object>() {
                    @Override
                    public int compare(Object str1, Object str2) {
                        if (Validate.isEmpty(desc)) {
                            return str1.toString().compareTo(str2.toString());
                        } else {
                            if (desc.trim().equalsIgnoreCase("asc")) {
                                return str2.toString().compareTo(str1.toString());
                            } else {
                                return str1.toString().compareTo(str2.toString());
                            }
                        }
                    }
                });

        sortMap.putAll(map);

        return sortMap;
    }
}