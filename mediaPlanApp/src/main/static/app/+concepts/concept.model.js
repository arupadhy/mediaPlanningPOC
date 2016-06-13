"use strict";
var Concept = (function () {
    function Concept(text, index, sentences, session, isNew) {
        this.name = text;
        this.sentences = sentences;
        this.index = index;
        this.sessionId = session;
        this.isNew = isNew;
    }
    return Concept;
}());
exports.Concept = Concept;
//# sourceMappingURL=C:/UI_STASH_REPO/LATEST/poc-development/tmp/broccoli_type_script_compiler-input_base_path-tYOsqpIh.tmp/0/app/+concepts/concept.model.js.map