$ErrorActionPreference = 'Stop'
$projectPath = $PSScriptRoot
$outputPath = Join-Path $projectPath 'out'
New-Item -ItemType Directory -Path $outputPath -Force | Out-Null
& javac -encoding UTF-8 --release 17 -d $outputPath (Join-Path $projectPath 'src\HangzhouMetroTicketSystem.java')
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
& java -cp $outputPath HangzhouMetroTicketSystem
exit $LASTEXITCODE
