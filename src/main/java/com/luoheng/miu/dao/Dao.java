package com.luoheng.miu.dao;

import java.util.List;
import java.util.Map;

public abstract class Dao<T> {

    abstract T add(T t);

    abstract T update(T t);

    abstract void delete(Map<String,String> params);

    abstract List<T> find(Map<String,String> params);

    public String generateCondition(Map<String,String> params){
        StringBuilder condition=new StringBuilder();
        if(params!=null&&params.size()!=0){
            condition.append(" WHERE ");
            for(Map.Entry<String,String> entry:params.entrySet()){
                condition.append(entry.getKey()+"="+"\'"+entry.getValue()+"\'"+" and ");
            }
            condition.delete(condition.lastIndexOf("a"),condition.length()-1);
            condition.append(";");
        }
        return condition.toString();
    }
}
