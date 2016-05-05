/*
 * Copyright (c) 2013, 2016 Oracle and/or its affiliates. All rights reserved. This
 * code is released under a tri EPL/GPL/LGPL license. You can use it,
 * redistribute it and/or modify it under the terms of the:
 *
 * Eclipse Public License version 1.0
 * GNU General Public License version 2
 * GNU Lesser General Public License version 2.1
 */
package org.jruby.truffle.stdlib.bigdecimal;

import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.object.DynamicObject;
import com.oracle.truffle.api.source.SourceSection;
import org.jruby.truffle.RubyContext;
import org.jruby.truffle.core.cast.IntegerCastNode;
import org.jruby.truffle.core.cast.IntegerCastNodeGen;
import org.jruby.truffle.core.cast.ToIntNode;
import org.jruby.truffle.language.RubyNode;
import org.jruby.truffle.language.constants.ReadConstantNode;

@NodeChildren({@NodeChild("module"), @NodeChild("name")})
public abstract class GetIntegerConstantNode extends RubyNode {

    public abstract int executeGetIntegerConstant(VirtualFrame frame, DynamicObject module, String name);

    @Specialization(guards = "isRubyModule(module)")
    public int doInteger(
            VirtualFrame frame,
            DynamicObject module,
            String name,
            @Cached("createReadConstantNode()") ReadConstantNode readConstantNode,
            @Cached("create()") ToIntNode toIntNode,
            @Cached("createIntegerCastNode()") IntegerCastNode integerCastNode) {
        final Object value = readConstantNode.readConstant(frame, module, name);
        return integerCastNode.executeCastInt(toIntNode.executeIntOrLong(frame, value));
    }

    protected ReadConstantNode createReadConstantNode() {
        return new ReadConstantNode(null, null, false, false, null, null);
    }

    protected IntegerCastNode createIntegerCastNode() {
        return IntegerCastNodeGen.create(null, null, null);
    }

}
