<h2>Steps to run the app</h2>
<p>The app uses some values data needs to be present for loading via Buildconfig</p>
<ol>
<li>Change you Android view to Project file in the directory</li>
<li>Right click > New > File</li>
<li>Put the name as apiKey.properties</li>
</ol>
<p>Now paste these in the apiKey.properties file</p>
<li>CLIENT_ID="XXXXXXXX"/li>
<li>CLIENT_SECRET="XXXXXXXX"</li>
<li>CALLBACK="XXXXXXXX"</li>

<p> You also have to add your own github callback in [AndroidManifest.xml](https://github.com/debajyotibasak/OauthGithub/blob/main/app/src/main/AndroidManifest.xml).</p>
<p> If you have callback url as githubdeb://callback</p>

```
<data
   android:host="callback"
   android:scheme="githubdeb" />
```

<p>Build the project and Run</p>