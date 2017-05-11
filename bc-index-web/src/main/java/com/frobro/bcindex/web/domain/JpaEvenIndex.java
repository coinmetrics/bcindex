package com.frobro.bcindex.web.domain;

import javax.persistence.Entity;

/**
 * Created by rise on 5/9/17.
 */
@Entity
public class JpaEvenIndex extends JpaIndex {

  public static JpaEvenIndex create() {
    JpaEvenIndex idx = new JpaEvenIndex();
    setTime(idx);
    return idx;
  }


}
