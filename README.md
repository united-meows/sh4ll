Shell._self.registerExec(<span class="hljs-keyword">new</span> InlineInput());
</code></pre>
<p class="has-line-data" data-line-start="15" data-line-end="16">Open/Close shell</p>
<pre><code class="has-line-data" data-line-start="17" data-line-end="20" class="language-java">Shell._self.open();
Shell._self.close(); <span class="hljs-comment">// force close shell</span>
</code></pre>
<p class="has-line-data" data-line-start="21" data-line-end="23">Theme selection<br>
shell comes with one built-in theme&lt;br&gt;</p>
<p class="has-line-data" data-line-start="24" data-line-end="25">DarkThemeSh4, …</p>
<pre><code class="has-line-data" data-line-start="26" data-line-end="29" class="language-java"><span class="hljs-comment">//note: default theme is already DarkThemeSh4</span>
Shell._self.setTheme(<span class="hljs-keyword">new</span> DarkThemeSh4());
</code></pre>
<p class="has-line-data" data-line-start="29" data-line-end="31">Custom TextBlocks<br>
//TODO:</p>
<p class="has-line-data" data-line-start="32" data-line-end="33">Exec System Examples&lt;br&gt;</p>
<pre><code class="has-line-data" data-line-start="34" data-line-end="71" class="language-java">
<span class="hljs-keyword">public</span> <span class="hljs-class"><span class="hljs-keyword">class</span> <span class="hljs-title">TestScript</span> <span class="hljs-keyword">extends</span> <span class="hljs-title">Exec</span> </span>{
    <span class="hljs-function"><span class="hljs-keyword">public</span> <span class="hljs-title">TestScript</span><span class="hljs-params">()</span> </span>{
        <span class="hljs-comment">// aliases, inline inputs, description, useThread</span>
        <span class="hljs-keyword">super</span>(<span class="hljs-keyword">new</span> String[]{<span class="hljs-string">"testscript"</span>, <span class="hljs-string">"test_script"</span>}, <span class="hljs-keyword">new</span> String[]{}, <span class="hljs-string">"just a test"</span>, <span class="hljs-keyword">true</span> <span class="hljs-comment">/* if you getting input from shell this parameter should be true (readChar, readLine)*/</span>);
    }
    
    <span class="hljs-annotation">@Override</span>
    <span class="hljs-function"><span class="hljs-keyword">public</span> <span class="hljs-keyword">byte</span> <span class="hljs-title">runExec</span><span class="hljs-params">(String fullText, String[] splitted)</span> </span>{
        Shell._self.writeLine(<span class="hljs-keyword">new</span> NormalTextBlock(<span class="hljs-string">"Enter your name"</span>));
        String name = Shell._self.readLine();
        Shell._self.writeLine(<span class="hljs-keyword">new</span> NormalTextBlock(<span class="hljs-string">"Enter your lastname"</span>));
        String last = Shell._self.readLine();
        
        Shell._self.writeLine(<span class="hljs-keyword">new</span> NormalTextBlock(<span class="hljs-string">"Name: "</span> + name + <span class="hljs-string">" lastname: "</span> + last);
        <span class="hljs-keyword">return</span> StateResult.EXIT_ON_SUCCESS; <span class="hljs-comment">/* EXIT_ON_ERROR, RUNNING_BACKGROUND, UNKNOWN_EXIT */</span>
    }
 }
 
 <span class="hljs-keyword">public</span> <span class="hljs-class"><span class="hljs-keyword">class</span> <span class="hljs-title">InlineInput</span> <span class="hljs-keyword">extends</span> <span class="hljs-title">Exec</span></span>{
    <span class="hljs-comment">// aliases, inline inputs, description, useThread</span>
    <span class="hljs-function"><span class="hljs-keyword">public</span> <span class="hljs-title">InlineInput</span><span class="hljs-params">()</span> </span>{
            <span class="hljs-keyword">super</span>(<span class="hljs-keyword">new</span> String[]{<span class="hljs-string">"inline"</span>}, <span class="hljs-keyword">new</span> String[]{<span class="hljs-string">"testvar1"</span>, <span class="hljs-string">"testvar2"</span>}, <span class="hljs-string">"inlin input test"</span>, <span class="hljs-keyword">false</span>);
    }
    
    <span class="hljs-annotation">@Override</span>
    <span class="hljs-function"><span class="hljs-keyword">public</span> <span class="hljs-keyword">byte</span> <span class="hljs-title">runExec</span><span class="hljs-params">(String fullText, String[] splitted)</span> </span>{
         <span class="hljs-comment">//inline hello there</span>
         String testvar1 = getInput(<span class="hljs-string">"testvar1"</span>); <span class="hljs-comment">// value: hello</span>
         String testvar2 = getInput(<span class="hljs-string">"testvar2"</span>); <span class="hljs-comment">// value: there</span>
         
         <span class="hljs-comment">// getInput returns null if empty</span>
         <span class="hljs-comment">// hasInput(var) - checks if empty</span>
         
    }
 }
</code></pre>
<p class="has-line-data" data-line-start="72" data-line-end="74">Built-in games<br>
//TODO:</p>
